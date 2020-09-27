$(function() {

    autoLeftNav();
    $(window).resize(function() {
        autoLeftNav();
    });

});

// 风格切换
$('.tpl-skiner-toggle').on('click', function() {
    $('.tpl-skiner').toggleClass('active');
});

const clickChangeTheme = function () {
    let currentClass = $('body').attr('class');
    if (currentClass === 'theme-white'){
        currentClass = 'theme-black';
    }else{
        currentClass = 'theme-white';
    }
    changeThemeColor(currentClass);
    const $currentUrl = window.location.pathname;
    if ($currentUrl === '' || $currentUrl === '/center' || $currentUrl === '/index'){
        reloadAllStatement();
    }
}

const changeThemeColor = function ($chooseColor) {
    $('body').attr('class', $chooseColor);
    saveSelectColor.Color = $chooseColor;
    const $tab = $('#0neBean-frame-container');
    const $bd = $tab.find('.am-tabs-bd');
    const $frames = $bd.find('iframe');
    $.each($frames, function (index, item) {
        $(item).contents().find('body').attr('class', $chooseColor);
    });
    // 保存选择项
    storageSave(saveSelectColor);
};

$('.tpl-skiner-content-bar').find('span').on('click', function () {
    const $chooseColor = $(this).attr('data-color');
    changeThemeColor($chooseColor);
});

// 侧边菜单开关
function autoLeftNav() {
    var $leftSidebar = $('.left-sidebar');
    $('.tpl-header-switch-button').on('click', function() {
        if ($leftSidebar.is('.active')) {
            if ($(window).width() > 1024) {
                $('.tpl-content-wrapper').removeClass('active');
            }
            $leftSidebar.removeClass('active');
        } else {

            $leftSidebar.addClass('active');
            if ($(window).width() > 1024) {
                $('.tpl-content-wrapper').addClass('active');
            }
        }
    });

    if ($(window).width() < 1024) {
        $leftSidebar.addClass('active');
    } else {
        $leftSidebar.removeClass('active');
    }
}


// 侧边菜单
$('body').on('click', '.sidebar-nav-sub-title',function() {
    $(this).siblings('.sidebar-nav-sub').slideToggle(80).end().find('.sidebar-nav-sub-ico').toggleClass('sidebar-nav-sub-ico-rotate');

});