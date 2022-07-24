/* 年月ライブラリ */
$('#monthYear').datepicker({
    orientation: 'bottom',
    minViewMode: 1,
    startView: 1,
    format: 'yyyymm',
    language: 'ja',
});


/* ステータス削除確認ダイアログ */
$('#register').click(function () {
    let monthYear = document.getElementById('monthYear');
    let message = ''

    if (monthYear.value == "") {
        message = '年月が未入力です。'
    } else {
        message = '月分のステータスを登録します。宜しいですか？'
    }

    if (!confirm(monthYear.value + message)) {
        /* キャンセルの時の処理 */
        return false;
    } else {
        /* OKの時の処理 */
        return true;
    }
});

/*アップロードファイル上限設定 */
const maxFileSize = 10485760 //アップロードできる最大サイズを指定(10485760=10MB)
$("input[type=file]").change(function () { //ファイルがアップロードされたら
    $(".error_msg").remove() //エラーメッセージ削除
    let uploaded_file = $(this).prop('files')[0]; //アップロードファイル取得
    if (maxFileSize < uploaded_file.size) { //もし上限値を超えた場合
        $(this).val("") //画像を空にする
        $(this).after("<p class='error_msg text-danger'>10MB以下でアップロードしてください。</p>") //エラーメッセージ表示
    }
})