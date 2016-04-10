/*
 * Copyright 2016 Kasije Framework.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

(function(win, $)
{
    //Maps component id vs component instance
    var components = {};
    var componentsCount = 0;

    var Component = function(config)
    {
        config = config || {};
        config.id = config.id || 'comp-' + componentsCount;
        config.init = config.init || function(){};

        for (var key in config)
        {
            this[key] = config[key];
        }

        this.prototype = Component.prototype;

        components[this.id] = this;
        componentsCount++;

        return this;
    };

    Component.prototype.fire = function(event)
    {
        if (typeof(event) === 'string')
        {
            event = {
                id: event,
                data: {}
            };
        }

        var eventId = '_on' + event.id;

        for (var id in components)
        {
            var component = components[id];
            if (component[eventId] && typeof(component[eventId]) === 'function')
            {
                component[eventId].call(component, event.data);
            }
        }
    };
    Component.prototype.on = function(eventId, handler)
    {
        if (!eventId || !handler || typeof(handler) !== 'function')
        {
            return;
        }

        this['_on' + eventId] = handler;
    };

    win.Component = Component;

    var loadingEl = $('#loadingoverlay');
    win.showLoading = function()
    {
        if (loadingEl.length && !loadingEl.hasClass('loadingoverlay'))
        {
            loadingEl.addClass('loadingoverlay');
        }
    };
    win.hideLoading = function()
    {
        if (loadingEl.length && loadingEl.hasClass('loadingoverlay'))
        {
            loadingEl.removeClass('loadingoverlay');
        }
    };

    //Init components on jQuery load
    $(function()
    {
        for (var index in components)
        {
            var component = components[index];
            component.init.call(component);
        }
        win.hideLoading();
    });
})(window, jQuery);
