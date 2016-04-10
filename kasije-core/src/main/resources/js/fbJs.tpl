<#ftl encoding="UTF-8">

<#macro script btnSelector blockSelector userSelector userPreText loginSelector
        appId version lang="en_US">
<script>
    jQuery(function($)
    {
        'use strict';

        var fbButton = $('${btnSelector}');
        if (!fbButton.length)
        {
            return;
        }

        var scriptFb = $('#facebook-jssdk');
        if (scriptFb.length)
        {
            return;
        }

        var fbUser = $('${userSelector}');
        var fbLogin = $('${loginSelector}');

        var updateFbInfo = function ()
        {
            FB.api('/me', function (response)
            {
                fbUser.html('${userPreText}' + response.name);
                FB.XFBML.parse($('${blockSelector}')[0]);
            });
        };

        var doFbLogin = function ()
        {
            FB.login(updateStatus, {scope: 'public_profile,email'});
        };

        var updateStatus = function (response, doLogin)
        {
            var cookie = '';
            if (response.status === 'connected')
            {
                fbUser.removeClass('hidden');
                fbLogin.addClass('hidden');
                updateFbInfo();

                cookie = response.authResponse.accessToken;
            }
            else
            {
                fbUser.addClass('hidden');
                fbLogin.removeClass('hidden');
                if (doLogin)
                {
                    doFbLogin();
                }

                cookie = ';expires=Thu, 01 Jan 1970 00:00:01 GMT';
            }

            document.cookie = 'atfb=' + cookie + '; Path=/;';
        };

        window.fbAsyncInit = function ()
        {
            if (!FB)
            {
                return;
            }

            FB.init({
                appId: '${appId}',
                xfbml: false,
                version: '${version}',
                status: true,
                cookie: true
            });

            fbButton.removeClass('disabled');

            FB.getLoginStatus(updateStatus);
        };

        var scriptEl = document.createElement('script');
        scriptEl.id = 'facebook-jssdk';
        scriptEl.src = '//connect.facebook.net/${lang}/sdk.js';

        $('body').append(scriptEl);

        fbButton.on('click', function ()
        {
            if (fbButton.hasClass('disabled') || !FB)
            {
                return;
            }

            FB.getLoginStatus(function (response)
            {
                updateStatus(response, true);
            });
        });
    });
</script>
</#macro>
