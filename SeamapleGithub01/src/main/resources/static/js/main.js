// ブラウザバック禁止
history.pushState(null, null);
$(window).on('popstate', function (e) {
    history.pushState(null, null);
    alert('ブラウザの戻るボタンは使用できません');
});


// フォーム二重送信防止
$("form").submit(function () {
    var self = this;
    $(":submit", self).prop("disabled", true);
    setTimeout(function () {
        $(":submit", self).prop("disabled", false);
    }, 10000);
});


// スマホ判定 スマホ画面切り替え
if (navigator.userAgent.match(/iPhone|Android.+Mobile/)) {
    console.log("mobile");
    $('#header').addClass('mobile');
    $('#app').addClass('mobile');
} else {
    console.log("pc");
}

