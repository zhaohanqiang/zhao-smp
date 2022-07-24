/* 管理者用検索フォームリセット */
function resetForm() {
    document.getElementById('searchUserId').value = '';
    document.getElementById('searchUserName').value = '';
    document.getElementById('searchStartDate').value = '';
    document.getElementById('searchAppStatus').value = '';
    document.getElementById('searchEndDate').value = '';
}

/* 社員用検索フォームリセット */
function resetUserForm() {
    document.getElementById('searchStartDate').value = '';
    document.getElementById('searchEndDate').value = '';
    document.getElementById('searchAppStatus').value = '';
}

/* ステータス申請中ボタン活性 */
const $updateButtons = document.getElementsByClassName('update');
const updateButtonLen = $updateButtons.length;

let i = 0;
while (i < updateButtonLen) {
    if ($updateButtons[i].value == 0) {
        $updateButtons[i].style.display = 'block';
    }
    i++
}

/* テーブルソート機能 */
$('table.pager-table')
    .tablesorter({})
    .tablesorterPager({
        container: $(".pager"),
    });

/* ステータス承認確認ダイアログ */
$('.approval').click(function () {
    if (!confirm('ステータスを承認しますか？')) {
        /* キャンセルの時の処理 */
        return false;
    } else {
        /* OKの時の処理 */
        return true;
    }
});

/* ステータス承認否認ダイアログ */
$('.dismiss').click(function () {
    if (!confirm('ステータスを否認しますか？')) {
        /* キャンセルの時の処理 */
        return false;
    } else {
        /* OKの時の処理 */
        return true;
    }
});

/* ステータス全承認確認ダイアログ */
$('#appAll').click(function () {
    if (!confirm('ステータスを全て承認しますか？')) {
        /* キャンセルの時の処理 */
        return false;
    } else {
        /* OKの時の処理 */
        return true;
    }
});