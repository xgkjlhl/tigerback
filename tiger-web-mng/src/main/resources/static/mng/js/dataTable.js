/**
 * Created by hupeng on 2015/10/25.
 */

var defaultConfig = {
    "processing": true,
    "serverSide": true,
    "searchDelay": 1000,
    "paginationType": "full_numbers",
    "lengthMenu": [5, 10, 20, 50],
    "ordering": false,
    "bFilter": false, //列筛序功能禁用
//	"autoWidth": true,
//	"ajax": {
//		"url":'demoUrl',
//		"data":	function(d){
//			var o = {};
//			o.draw = d.draw;
//			o.page = d.start / d.length +1;
//			o.rows = d.length;
//			o.fuzzyName = d.search.value;
//			return o;
//		}
//	},
    "language": {
        "lengthMenu": "每页显示 _MENU_ 条数据",
        "zeroRecords": "没有找到任何记录",
        "info": "显示 第 _PAGE_ 页， 共 _PAGES_ 页，  共_TOTAL_条",
        "infoEmpty": "没有找到任何记录",
        "processing": "处理中...",
        "paginate": {
            "first": "首页",
            "previous": "前一页",
            "next": "后一页",
            "last": "尾页"
        }
    },
    "columns": [],
    "fnCreatedRow": function (nRow, aData, iDataIndex) {
        //add selected class
        $(nRow).click(function () {
            if ($(this).hasClass('row_selected')) {
                $(this).removeClass('row_selected');
            } else {
                $('tr.row_selected').removeClass('row_selected');
                $(this).addClass('row_selected');
            }
        });
    }
}

var drawDT = function (tableId, ajax, columns, callBack) {
    var config = defaultConfig;
    config.ajax = ajax;
    config.columns = columns;
    config.fnDrawCallback = callBack;
    var dt = $("#" + tableId).DataTable(config);
    return dt;
}

