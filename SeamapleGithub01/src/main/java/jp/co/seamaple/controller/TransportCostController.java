package jp.co.seamaple.controller;

import jp.co.seamaple.entity.SessionData;
import jp.co.seamaple.entity.TransportCost;
import jp.co.seamaple.form.DateForm;
import jp.co.seamaple.form.TransportCostAppForm;
import jp.co.seamaple.form.TransportCostListForm;
import jp.co.seamaple.repository.TransportCostRepository;
import jp.co.seamaple.repository.TransportCostRepository.TotalCostList;
import jp.co.seamaple.service.TransportCostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class TransportCostController {

    private final TransportCostService transportCostService;
    private final TransportCostRepository transportCostRepository;
    private final SessionData sessionData;

    @Autowired
    public TransportCostController(TransportCostService transportCostService,
                                   TransportCostRepository transportCostRepository, SessionData sessionData) {
        this.transportCostService = transportCostService;
        this.transportCostRepository = transportCostRepository;
        this.sessionData = sessionData;
    }

    // 交通費申請 初期表示
    @PostMapping("/transportCostApp")
    public String getTransportCostApp(@RequestParam(name = "errorFlag", required = false) String errorFlag,
                                      @RequestParam(name = "savetmp", required = false) String savetmp,
                                      @RequestParam(name = "monthYear", required = false) String monthYear,
                                      @ModelAttribute DateForm dateForm,
                                      @ModelAttribute TransportCostAppForm transportCostAppForm,
                                      @ModelAttribute TransportCostListForm transportCostListForm, Model model) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        model.addAttribute("types", transportCostService.getTypes());
        model.addAttribute("days", transportCostService.getDays());

        // 管理者用 表示
        if ("admin".equals(sessionData.getRoleId())) {

            if (dateForm.getUserName() != null) {
                model.addAttribute("userName", "氏名 " + dateForm.getUserName());
            }

            if (monthYear != null) {
                String sdfmonthYear = sdf.format(dateForm.getMonthYear());
                model.addAttribute("monthYear", sdfmonthYear);

                List<TransportCost> costLists = transportCostRepository.getByDateAndUserId(sdfmonthYear,
                        dateForm.getUserId());
                model.addAttribute("transportCostListForm", costLists);
            } else {
                String now = sdf.format(new Date());
                model.addAttribute("monthYear", now);
                List<TransportCost> costLists = transportCostRepository.getByDateAndUserId(now,
                        sessionData.getUserId());
                model.addAttribute("transportCostListForm", costLists);
            }
            return "transportcost/transportCostApp";
        }

        // 一般ユーザー用 表示
        if ("true".equals(errorFlag)) {
            model.addAttribute("errorMessage", "入力に誤りがあります。");
        }
        if ("true".equals(savetmp)) {
            model.addAttribute("savetmp", "一時保存されました。");
        }

        // 年月検索フォームから遷移
        if (monthYear != null) {
            String sdfmonthYear = sdf.format(dateForm.getMonthYear());
            model.addAttribute("monthYear", sdfmonthYear);
            List<TransportCost> costLists = transportCostRepository.getByDateAndUserId(sdfmonthYear,
                    sessionData.getUserId());
            model.addAttribute("transportCostListForm", costLists);

            // 合計金額表示
            if (costLists.size() != 0) {
                int totalCost = transportCostRepository.getTotalByDateAndUserId(sdfmonthYear,
                        sessionData.getUserId());
                model.addAttribute("totalCost", "計 " + totalCost + " 円");
            }
        }
        // 初期画面(現在年月で参照)
        else {
            String now = sdf.format(new Date());
            model.addAttribute("monthYear", now);
            List<TransportCost> costLists = transportCostRepository.getByDateAndUserId(now,
                    sessionData.getUserId());
            model.addAttribute("transportCostListForm", costLists);

            if (costLists.size() != 0) {
                int totalCost = transportCostRepository.getTotalByDateAndUserId(now,
                        sessionData.getUserId());
                model.addAttribute("totalCost", "計 " + totalCost + " 円");
            }
        }
        return "transportcost/transportCostApp";
    }

    // 交通費申請確定
    @PostMapping(value = "/transportCostApp/confirm")
    public String confirmList(@ModelAttribute TransportCostListForm transportCostListForm,
                              @Validated @ModelAttribute TransportCostAppForm transportCostAppForm, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "forward:/transportCostApp?errorFlag=true";
        }
        transportCostService.insertFeeList(transportCostListForm);
        return "forward:/transportCostApp";
    }

    // 交通費申請(在宅)
    @PostMapping(value = "/atHome")
    public String confirmAtHome(@RequestParam(name = "monthYear", required = false) String monthYear) {
        transportCostService.insertFeeAtHome(monthYear);
        return "forward:/transportCostApp";
    }

    // 交通費一時保存
    @PostMapping(value = "/transportCostApp/savetmp")
    public String savetmp(@ModelAttribute TransportCostListForm transportCostListForm,
                          @Validated @ModelAttribute TransportCostAppForm transportCostAppForm, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "forward:/transportCostApp?errorFlag=true";
        }
        transportCostService.inserttmpList(transportCostListForm);
        return "forward:/transportCostApp?savetmp=true";
    }

    // 月別交通費一覧表示
    @PostMapping(value = "/transportCostList")
    public String getCostList(@ModelAttribute DateForm dateForm, Model model) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        if (dateForm.getMonthYear() != null) {
            String formattedDate = sdf.format(dateForm.getMonthYear());
            List<TotalCostList> costList = transportCostService.getTotalCostList(formattedDate);
            model.addAttribute("monthYear", formattedDate);
            model.addAttribute("transportCostLists", costList);
        } else {
            String dateNow = sdf.format(new Date());
            List<TotalCostList> costList = transportCostService.getTotalCostList(dateNow);
            model.addAttribute("monthYear", dateNow);
            model.addAttribute("transportCostLists", costList);
        }
        return "transportcost/transportCostList";
    }

    // 交通費情報 確定ステータス解除
    @PostMapping(value = "updateDisabled")
    public String updateDisabled(@ModelAttribute DateForm dateForm) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        String formattedDate = sdf.format(dateForm.getMonthYear());
        transportCostService.updateDisabledStatus(formattedDate, dateForm.getUserId());
        return "forward:/transportCostList";

    }
     // 年別交通費一覧表示  管理者用 表示
     @GetMapping(value = "/transportCostList2")
     public String getCostList1(@ModelAttribute DateForm dateForm2, Model model) {
         SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
         if (dateForm2.getMonthYear() != null) {
             String formattedDate = sdf.format(dateForm2.getMonthYear());
             List<TotalCostList> costList = transportCostService.getTotalCostList1(formattedDate);
             model.addAttribute("monthYear", formattedDate);
             model.addAttribute("transportCostLists", costList);
         } else {
             String dateNow = sdf.format(new Date());
             List<TotalCostList> costList = transportCostService.getTotalCostList1(dateNow);
             model.addAttribute("monthYear", dateNow);
             model.addAttribute("transportCostLists", costList);
         }
         return "transportcost/transportCostList2";
     }
     @PostMapping(value = "/transportCostList2")
     public String getCostList2(@ModelAttribute DateForm dateForm2, Model model) {
         SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
         if (dateForm2.getMonthYear() != null) {
             String formattedDate = sdf.format(dateForm2.getMonthYear());
             List<TotalCostList> costList = transportCostService.getTotalCostList1(formattedDate);
             model.addAttribute("monthYear", formattedDate);
             model.addAttribute("transportCostLists", costList);
         } else {
             String dateNow = sdf.format(new Date());
             List<TotalCostList> costList = transportCostService.getTotalCostList1(dateNow);
             model.addAttribute("monthYear", dateNow);
             model.addAttribute("transportCostLists", costList);
         }
         return "transportcost/transportCostList2";
     }

     // 年別交通費一覧表示  

     @PostMapping(value = "/transportCostList3")
     public String getCostList4(@ModelAttribute DateForm dateForm2, Model model) {
         SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
         if (dateForm2.getMonthYear() != null) {
             String formattedDate = sdf.format(dateForm2.getMonthYear());
             List<TotalCostList> costList = transportCostService.getTotalCostList1(formattedDate);
             model.addAttribute("monthYear", formattedDate);
             model.addAttribute("transportCostLists", costList);
         } else {
             String dateNow = sdf.format(new Date());
             List<TotalCostList> costList = transportCostService.getTotalCostList1(dateNow);
             model.addAttribute("monthYear", dateNow);
             model.addAttribute("transportCostLists", costList);
         }
         return "transportcost/transportCostList3";
     }
}
