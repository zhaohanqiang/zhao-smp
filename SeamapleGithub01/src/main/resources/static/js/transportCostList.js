/* 年月表示カレンダー */
$('#monthYear').datepicker({
    orientation: 'bottom',
    minViewMode: 1,
    startView: 1,
    format: 'yyyymm',
    language: 'ja',
});

/* ステータス解除確認ダイアログ */
$('.unlockStatus').click(function () {
    if (!confirm('確定ステータスを解除しますが、よろしいですか？')) {
        /* キャンセルの時の処理 */
        return false;
    } else {
        /* OKの時の処理 */
        return true;
    }
});

/* テーブルソート機能 */
$(document).ready(function () {
    $('#transportCostList').tablesorter();
});