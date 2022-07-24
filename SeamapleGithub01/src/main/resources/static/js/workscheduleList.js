/* 管理者用年月表示 */
$('#monthYear').datepicker({
    orientation: 'bottom',
    minViewMode: 1,
    startView: 1,
    format: 'yyyymm',
    language: 'ja',
});

/* テーブルソート機能 */
$('table.pager-table')
    .tablesorter({})
    .tablesorterPager({
        container: $(".pager"),
    });

/* ステータス削除確認ダイアログ */
$('.delete').click(function () {
    if (!confirm('ステータスを削除します。宜しいですか？')) {
        /* キャンセルの時の処理 */
        return false;
    } else {
        /* OKの時の処理 */
        return true;
    }
});


// 備考モーダル
$('#exampleModal').on('show.bs.modal', function (event) {
    let button = $(event.relatedTarget);
    var sampledata = button.val();
    var modal = $(this);
    // let memo = modal.find('.modal-body').text(sampledata);
    modal.find('.modal-body').text(sampledata);
})