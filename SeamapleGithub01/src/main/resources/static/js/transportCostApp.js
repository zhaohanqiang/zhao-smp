// 行末複製
const $copyRow = document.getElementById('copyRow');
const $tbody = document.getElementById('feeBody');
$copyRow.addEventListener('click', () => {
    let clone = $tbody.lastElementChild.cloneNode(true);
    $tbody.appendChild(clone)
})

// 行新規
const $insertRow = document.getElementById('insertRow');
$insertRow.addEventListener('click', () => {
    let row = $("table tbody tr:first").clone();
    row.find('input:text').val('');
    row.find('select').val('');
    $("table tbody").append(row);
})

//行削除
function delRow(obj) {
    if ($('#feeTable tbody tr').length > 1) {
        tr = obj.parentNode.parentNode;
        tr.parentNode.deleteRow(tr.sectionRowIndex);
    }
}

const $office = document.getElementById('office');
const $home = document.getElementById('home');
const $insertForm = document.getElementById('insertForm');

/* 初期画面 在宅申請ボタン非活性 */
const $homeFormButton = document.getElementById('homeFormButton');
$homeFormButton.style.display = 'none';

/* 在宅時選択済みなら"在宅のみ"をchecked */
const $types = document.getElementById('types');
// 初期状態でoption"在宅"を非表示
$types[4].style.display = 'none';
if ($types.value == 3) {
    $office.checked = false;
    $home.checked = true;
}

// 交通費（在宅）フォーム表示切替
function formSwitch() {
    // 在宅申請時
    if ($home.checked == true) {
        $insertForm.style.display = 'none';
        $homeFormButton.style.display = 'block';
    }
    // 出社申請時
    if ($office.checked == true) {
        $insertForm.style.display = 'block';
        $homeFormButton.style.display = 'none';
    }
}

/* ステータス変更確認ダイアログ */
$('.confirm').click(function () {
    let monthYear = document.getElementById('confirm_date').value;
    if (!confirm(monthYear + '月分の申請です。確定後修正は出来ません。宜しいですか？')) {
        /* キャンセルの時の処理 */
        return false;
    } else {
        /* OKの時の処理 */
        return true;
    }
});

/* datepicker 月別表示 */
$('#monthYear').datepicker({
    orientation: 'bottom',
    minViewMode: 1,
    startView: 1,
    format: 'yyyymm',
    language: 'ja',
});

// 確定日入力不可
let bool = document.getElementsByClassName('disabled')[0].value;
if (bool) {
    console.log("確定");
    // 各ボタン非活性
    $('.input').prop('disabled', true);
} else {
    console.log('未確定');
    // 各ボタン活性化
    $('.input').addClass('active');
}