/**
 * Created by alfred_yuan on 5/4/16.
 */
void

function() {
    /**
     * 页面导航部分
     */
    /**
     * 系统配置
     */
    // 用户管理页面
    $("#nav_system_account").click(function() {
        navAjax("/accounts");
    });

    // 系统参数管理页面
    $("#nav_system_params").click(function() {
        navAjax("/systemParams");
    });

    // 管理员管理页面
    $("#nav_system_staffs").click(function() {
        navAjax("/staffs");
    });

    /**
     * 页面管理
     */
    // 图片列表页面
    $("#nav_page_pic_list").click(function() {
        navAjax("/portalConfig");
    });

    /**
     * 用户沟通
     */
    // 反馈列表
    $("#nav_communicate_feedback_list").click(function() {
        navAjax("/feedbacks");
    });

    /**
     * 管理员下拉菜单
     */
    // 更改密码
    $("#dropdown_password_update").click(function() {
        navAjax("/staff/password");
    });

    /**
     * 其他事件控制
     */

    // 控制左侧active事件
    var nav_li_items = $("ul.treeview-menu li");
    nav_li_items.click(function() {
        nav_li_items.removeClass("active");
        $(this).addClass("active");
    });

    /**
     * 方法区域
     */
    // get ajax 方法请求方法，导航菜单使用
    function navAjax(url) {
        $.ajax(url)
            .done(function(result) {
                $("#main_content").html(result);
            });
    }

    // 失败监听
    $(document).ajaxError(function(event, request, settings) {
        if (request.status === 301) {
            document.open();
            document.write(request.responseText);
            document.close();

            event.preventDefault();
        }
        if (TigerMessage) {
            TigerMessage.showError("Error Requesting Page " + settings.url + "!");
        }
    });
}();
