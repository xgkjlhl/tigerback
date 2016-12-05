/**
 * Created by alfred_yuan on 1/29/16.
 */
void

function() {
    if (!this.TigerUtil) {
      this.TigerUtil = {};
      (function() {
        function getDate(date) {
          if (typeof date === 'string' || date instanceof String) {
            return new Date(date);
          } else if (typeof date === 'date' || date instanceof Date) {
            return date;
          }
          return new Date();
        }

        // 格式化日期， 格式为 yyyy年MM月dd日
        TigerUtil.formatDate = function(timeStamp) {
          var d = getDate(date),
              month = '' + (d.getMonth() + 1),
              day = '' + d.getDate(),
              year = d.getFullYear();

          if (month.length < 2) month = '0' + month;
          if (day.length < 2) day = '0' + day;

          return year + "年" + month + "月" + day + "日";
        };

        // 格式化日期， 格式为 yyyy-MM-dd
        TigerUtil.webFormatDate = function(date) {
            var d = getDate(date),
                month = '' + (d.getMonth() + 1),
                day = '' + d.getDate(),
                year = d.getFullYear();

            if (month.length < 2) month = '0' + month;
            if (day.length < 2) day = '0' + day;

            return [year, month, day].join('-');
        };

        // 将 yyyy年MM月dd日 转换为 yyyy-MM-dd
        TigerUtil.DateToString = function(dateStr) {
            return dateStr.trim().slice(0, -1).replace(/年|月|日/g, '-');
        };

        // 将form的内容转化为json
        TigerUtil.FormToJSON = function(formID) {
            var tempObj;
            if (formID === null || !(tempObj = $(formID)).is("form")) {
                return;
            }
            var o = {};
            var a = tempObj.serializeArray();
            $.each(a, function() {
                if (o[this.name]) {
                    if (!o[this.name].push) {
                        o[this.name] = [o[this.name]];
                    }
                    o[this.name].push(this.value || '');
                } else {
                    o[this.name] = this.value || '';
                }
            });
            return o;
        };

        TigerUtil.getDate = getDate;
    })();
  }
}();
