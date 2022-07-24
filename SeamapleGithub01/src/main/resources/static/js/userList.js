/* テーブルソート機能 */
$(document).ready(function () {
    $('#userTable').tablesorter({
        buttons: {
            0: {buttons: false}
        }
    });
});

/* 退職者グレーアウト */
const lists = document.getElementsByClassName('userList');
for (i = 0; i < lists.length; i++) {
    let userStatus = document.getElementsByClassName('enabled');
    if (userStatus[i].value != 1) {
        lists[i].style.backgroundColor = '#E6E6E6';
        // テーブルソート機能でホバー箇所に色がつくため無効化
        lists[i].style.pointerEvents = 'none';
    }
}

/* 社員一覧表示切替 *display = 'block'は空白ができるため× */
function changeList(view) {
    let userStatus = document.getElementsByClassName('enabled');

    // 現職者のみ表示
    if (view == 'activeOnly') {
        for (i = 0; i < lists.length; i++) {
            if (userStatus[i].value != 1) {
                lists[i].style.display = 'none';
            } else {
                lists[i].style.display = '';
            }
        }

        // 退職者のみ表示
    } else if (view == 'deactiveOnly') {
        for (i = 0; i < lists.length; i++) {
            if (userStatus[i].value == 1) {
                lists[i].style.display = 'none';
            } else {
                lists[i].style.display = '';
            }
        }

        // 全表示
    } else {
        for (i = 0; i < lists.length; i++) {
            lists[i].style.display = '';
        }
    }
}

/* アカウントロック確認ダイアログ */
// $('.lock').click(function () {
//     if (!confirm('この社員をロックします。よろしいですか？')) {
//         /* キャンセルの時の処理 */
//         return false;
//     } else {
//         /* OKの時の処理 */
//         return true;
//     }
// });

// 備考モーダル
$('#exampleModal').on('show.bs.modal', function (event) {
    let button = $(event.relatedTarget);
    var sampledata = button.val();
    var modal = $(this);
    modal.find('.modal-title').text("退職社員番号: " + sampledata);
    modal.find('.modalUserId').val(sampledata);
})

/* アカウントロック確認ダイアログ */
$('.delete').click(function () {
    if (!confirm('この社員を削除します。作業は元に戻せません。本当によろしいですか？')) {
        /* キャンセルの時の処理 */
        return false;
    } else {
        /* OKの時の処理 */
        return true;
    }
});