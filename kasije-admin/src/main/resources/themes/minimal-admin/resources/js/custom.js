//Loads the correct sidebar on window load,
//collapses the sidebar on window resize.
// Sets the min-height of #page-wrapper to window size
$(function()
{
    $('#side-menu')
        .metisMenu();

    $(window)
        .bind('load resize', function()
        {
            var topOffset = 50;
            var width = (this.window.innerWidth > 0) ? this.window.innerWidth : this.screen.width;
            if (width < 768)
            {
                $('div.navbar-collapse').addClass('collapse');
                topOffset = 100; // 2-row-menu
            }
            else
            {
                $('div.navbar-collapse').removeClass('collapse');
            }

            var height = ((this.window.innerHeight > 0) ? this.window.innerHeight : this.screen.height) - 1;
            height -= topOffset;
            if (height < 1)
            {
                height = 1;
            }
            if (height > topOffset)
            {
                $('#page-wrapper')
                    .css('min-height', (height) + 'px');
            }
        });

    var url = window.location;
    var element = $('ul.nav a')
        .filter(function()
        {
            return this.href == url || url.href.indexOf(this.href) == 0;
        })
        .addClass('active').parent().parent()
        .addClass('in').parent();

    if (element.is('li'))
    {
        element.addClass('active');
    }

    $('#supported')
        .text('Supported/allowed: ' + !!screenfull.enabled);

    if (!screenfull.enabled)
    {
        return false;
    }

    $('#toggle').click(function ()
    {
        screenfull.toggle($('#container')[0]);
    });

    // custom scrollbar
    $('html')
        .niceScroll({
            styler: 'fb',
            cursorcolor: '#1ABC9C',
            cursorwidth: '6',
            cursorborderradius: '10px',
            background: '#424f63',
            spacebarenabled:false,
            cursorborder: '0',
            zindex: '1000'
        });

    $('.scrollbar1')
        .niceScroll({
            styler: 'fb',
            cursorcolor: 'rgba(97, 100, 193, 0.78)',
            cursorwidth: '6',
            cursorborderradius: '0',
            autohidemode: 'false',
            background: '#F1F1F1',
            spacebarenabled:false,
            cursorborder: '0'
        });

    $('.scrollbar1').getNiceScroll();
    if ($('body').hasClass('scrollbar1-collapsed'))
    {
        $('.scrollbar1').getNiceScroll().hide();
    }
});
