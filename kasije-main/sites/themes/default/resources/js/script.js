!function(t){function e(){var e,i,n={height:a.innerHeight,width:a.innerWidth};return n.height||(e=r.compatMode,(e||!t.support.boxModel)&&(i="CSS1Compat"===e?f:r.body,n={height:i.clientHeight,width:i.clientWidth})),n}function i(){return{top:a.pageYOffset||f.scrollTop||r.body.scrollTop,left:a.pageXOffset||f.scrollLeft||r.body.scrollLeft}}function n(){var n,l=t(),r=0;if(t.each(d,function(t,e){var i=e.data.selector,n=e.$element;l=l.add(i?n.find(i):n)}),n=l.length)for(o=o||e(),h=h||i();n>r;r++)if(t.contains(f,l[r])){var a,c,p,s=t(l[r]),u={height:s.height(),width:s.width()},g=s.offset(),v=s.data("inview");if(!h||!o)return;g.top+u.height>h.top&&g.top<h.top+o.height&&g.left+u.width>h.left&&g.left<h.left+o.width?(a=h.left>g.left?"right":h.left+o.width<g.left+u.width?"left":"both",c=h.top>g.top?"bottom":h.top+o.height<g.top+u.height?"top":"both",p=a+"-"+c,v&&v===p||s.data("inview",p).trigger("inview",[!0,a,c])):v&&s.data("inview",!1).trigger("inview",[!1])}}var o,h,l,d={},r=document,a=window,f=r.documentElement,c=t.expando;t.event.special.inview={add:function(e){d[e.guid+"-"+this[c]]={data:e,$element:t(this)},l||t.isEmptyObject(d)||(l=setInterval(n,250))},remove:function(e){try{delete d[e.guid+"-"+this[c]]}catch(i){}t.isEmptyObject(d)&&(clearInterval(l),l=null)}},t(a).bind("scroll resize scrollstop",function(){o=h=null}),!f.addEventListener&&f.attachEvent&&f.attachEvent("onfocusin",function(){h=null})}(jQuery);jQuery(function($) {'use strict';

    // Navigation Scroll
    $(window).scroll(function(event) {
        Scroll();
    });

    $('.navbar-collapse ul li a').on('click', function() {
        $('html, body').animate({scrollTop: $(this.hash).offset().top - 5}, 1000);
        return false;
    });

    // User define function
    function Scroll() {
        var contentTop      =   [];
        var contentBottom   =   [];
        var winTop      =   $(window).scrollTop();
        var rangeTop    =   200;
        var rangeBottom =   500;
        $('.navbar-collapse').find('.scroll a').each(function(){
            contentTop.push( $( $(this).attr('href') ).offset().top);
            contentBottom.push( $( $(this).attr('href') ).offset().top + $( $(this).attr('href') ).height() );
        })
        $.each( contentTop, function(i){
            if ( winTop > contentTop[i] - rangeTop ){
                $('.navbar-collapse li.scroll')
                .removeClass('active')
                .eq(i).addClass('active');
            }
        })
    };

    $('#tohash').on('click', function(){
        $('html, body').animate({scrollTop: $(this.hash).offset().top - 5}, 1000);
        return false;
    });

    // accordian
    $('.accordion-toggle').on('click', function(){
        $(this).closest('.panel-group').children().each(function(){
        $(this).find('>.panel-heading').removeClass('active');
         });

         $(this).closest('.panel-heading').toggleClass('active');
    });

    //Slider
    $(document).ready(function() {
        var time = 7; // time in seconds

         var $progressBar,
          $bar,
          $elem,
          isPause,
          tick,
          percentTime;

        //Init the carousel
        $("#main-slider").find('.owl-carousel').owlCarousel({
          slideSpeed : 500,
          paginationSpeed : 500,
          singleItem : true,
          navigation : true,
            navigationText: [
            "<i class='fa fa-angle-left'></i>",
            "<i class='fa fa-angle-right'></i>"
            ],
          afterInit : progressBar,
          afterMove : moved,
          startDragging : pauseOnDragging,
          //autoHeight : true,
          transitionStyle : "fadeUp"
        });

        //Init progressBar where elem is $("#owl-demo")
        function progressBar(elem){
          $elem = elem;
          //build progress bar elements
          buildProgressBar();
          //start counting
          start();
        }

        //create div#progressBar and div#bar then append to $(".owl-carousel")
        function buildProgressBar(){
          $progressBar = $("<div>",{
            id:"progressBar"
          });
          $bar = $("<div>",{
            id:"bar"
          });
          $progressBar.append($bar).appendTo($elem);
        }

        function start() {
          //reset timer
          percentTime = 0;
          isPause = false;
          //run interval every 0.01 second
          tick = setInterval(interval, 10);
        };

        function interval() {
          if(isPause === false){
            percentTime += 1 / time;
            $bar.css({
               width: percentTime+"%"
             });
            //if percentTime is equal or greater than 100
            if(percentTime >= 100){
              //slide to next item
              $elem.trigger('owl.next')
            }
          }
        }

        //pause while dragging
        function pauseOnDragging(){
          isPause = true;
        }

        //moved callback
        function moved(){
          //clear interval
          clearTimeout(tick);
          //start again
          start();
        }
    });

    //Initiat WOW JS
    new WOW().init();
    //smoothScroll
    smoothScroll.init();

    // portfolio filter
    $(window).load(function(){'use strict';
        var $portfolio_selectors = $('.portfolio-filter >li>a');
        var $portfolio = $('.portfolio-items');
        $portfolio.isotope({
            itemSelector : '.portfolio-item',
            layoutMode : 'fitRows'
        });

        $portfolio_selectors.on('click', function(){
            $portfolio_selectors.removeClass('active');
            $(this).addClass('active');
            var selector = $(this).attr('data-filter');
            $portfolio.isotope({ filter: selector });
            return false;
        });
    });

    $(document).ready(function() {
        //Animated Progress
        $('.progress-bar').bind('inview', function(event, visible, visiblePartX, visiblePartY) {
            if (visible) {
                $(this).css('width', $(this).data('width') + '%');
                $(this).unbind('inview');
            }
        });

        //Animated Number
        $.fn.animateNumbers = function(stop, commas, duration, ease) {
            return this.each(function() {
                var $this = $(this);
                var start = parseInt($this.text().replace(/,/g, ""));
                commas = (commas === undefined) ? true : commas;
                $({value: start}).animate({value: stop}, {
                    duration: duration == undefined ? 1000 : duration,
                    easing: ease == undefined ? "swing" : ease,
                    step: function() {
                        $this.text(Math.floor(this.value));
                        if (commas) { $this.text($this.text().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,")); }
                    },
                    complete: function() {
                        if (parseInt($this.text()) !== stop) {
                            $this.text(stop);
                            if (commas) { $this.text($this.text().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,")); }
                        }
                    }
                });
            });
        };

        $('.animated-number').bind('inview', function(event, visible, visiblePartX, visiblePartY) {
            var $this = $(this);
            if (visible) {
                $this.animateNumbers($this.data('digit'), false, $this.data('duration'));
                $this.unbind('inview');
            }
        });
    });

    // Contact form
    var form = $('#main-contact-form');
    form.submit(function(event){
        event.preventDefault();
        var form_status = $('<div class="form_status"></div>');
        $.ajax({
            url: $(this).attr('action'),
            beforeSend: function(){
                form.prepend( form_status.html('<p><i class="fa fa-spinner fa-spin"></i> Email is sending...</p>').fadeIn() );
            }
        }).done(function(data){
            form_status.html('<p class="text-success">Thank you for contact us. As early as possible  we will contact you</p>').delay(3000).fadeOut();
        });
    });

    //Pretty Photo
    $("a[rel^='prettyPhoto']").prettyPhoto({
        social_tools: false
    });

    //Google Map
    var latitude = $('#google-map').data('latitude');
    var longitude = $('#google-map').data('longitude');
    function initialize_map() {
        var optLatlong = new google.maps.LatLng(latitude, longitude + .0075);
        var mapOptions = {
            zoom: 16,
            scrollwheel: false,
            center: optLatlong
        };
        var map = new google.maps.Map(document.getElementById('google-map'), mapOptions);
        var markerLatlong = new google.maps.LatLng(latitude, longitude);
        var marker = new google.maps.Marker({
            position: markerLatlong,
            map: map
        });
    }
    google.maps.event.addDomListener(window, 'load', initialize_map);

});(function (root, factory) {
    if ( typeof define === 'function' && define.amd ) {
        define('smoothScroll', factory(root));
    } else if ( typeof exports === 'object' ) {
        module.smoothScroll = factory(root);
    } else {
        root.smoothScroll = factory(root);
    }
})(this, function (root) {

    'use strict';

    //
    // Variables
    //

    var exports = {}; // Object for public APIs
    var supports = !!document.querySelector && !!root.addEventListener; // Feature test
    var settings;

    // Default settings
    var defaults = {
        speed: 500,
        easing: 'easeInOutCubic',
        offset: 0,
        updateURL: false,
        callbackBefore: function () {},
        callbackAfter: function () {}
    };


    //
    // Methods
    //

    /**
     * A simple forEach() implementation for Arrays, Objects and NodeLists
     * @private
     * @param {Array|Object|NodeList} collection Collection of items to iterate
     * @param {Function} callback Callback function for each iteration
     * @param {Array|Object|NodeList} scope Object/NodeList/Array that forEach is iterating over (aka `this`)
     */
    var forEach = function (collection, callback, scope) {
        if (Object.prototype.toString.call(collection) === '[object Object]') {
            for (var prop in collection) {
                if (Object.prototype.hasOwnProperty.call(collection, prop)) {
                    callback.call(scope, collection[prop], prop, collection);
                }
            }
        } else {
            for (var i = 0, len = collection.length; i < len; i++) {
                callback.call(scope, collection[i], i, collection);
            }
        }
    };

    /**
     * Merge defaults with user options
     * @private
     * @param {Object} defaults Default settings
     * @param {Object} options User options
     * @returns {Object} Merged values of defaults and options
     */
    var extend = function ( defaults, options ) {
        var extended = {};
        forEach(defaults, function (value, prop) {
            extended[prop] = defaults[prop];
        });
        forEach(options, function (value, prop) {
            extended[prop] = options[prop];
        });
        return extended;
    };

    /**
     * Calculate the easing pattern
     * @private
     * @param {String} type Easing pattern
     * @param {Number} time Time animation should take to complete
     * @returns {Number}
     */
    var easingPattern = function ( type, time ) {
        var pattern;
        if ( type === 'easeInQuad' ) pattern = time * time; // accelerating from zero velocity
        if ( type === 'easeOutQuad' ) pattern = time * (2 - time); // decelerating to zero velocity
        if ( type === 'easeInOutQuad' ) pattern = time < 0.5 ? 2 * time * time : -1 + (4 - 2 * time) * time; // acceleration until halfway, then deceleration
        if ( type === 'easeInCubic' ) pattern = time * time * time; // accelerating from zero velocity
        if ( type === 'easeOutCubic' ) pattern = (--time) * time * time + 1; // decelerating to zero velocity
        if ( type === 'easeInOutCubic' ) pattern = time < 0.5 ? 4 * time * time * time : (time - 1) * (2 * time - 2) * (2 * time - 2) + 1; // acceleration until halfway, then deceleration
        if ( type === 'easeInQuart' ) pattern = time * time * time * time; // accelerating from zero velocity
        if ( type === 'easeOutQuart' ) pattern = 1 - (--time) * time * time * time; // decelerating to zero velocity
        if ( type === 'easeInOutQuart' ) pattern = time < 0.5 ? 8 * time * time * time * time : 1 - 8 * (--time) * time * time * time; // acceleration until halfway, then deceleration
        if ( type === 'easeInQuint' ) pattern = time * time * time * time * time; // accelerating from zero velocity
        if ( type === 'easeOutQuint' ) pattern = 1 + (--time) * time * time * time * time; // decelerating to zero velocity
        if ( type === 'easeInOutQuint' ) pattern = time < 0.5 ? 16 * time * time * time * time * time : 1 + 16 * (--time) * time * time * time * time; // acceleration until halfway, then deceleration
        return pattern || time; // no easing, no acceleration
    };

    /**
     * Calculate how far to scroll
     * @private
     * @param {Element} anchor The anchor element to scroll to
     * @param {Number} headerHeight Height of a fixed header, if any
     * @param {Number} offset Number of pixels by which to offset scroll
     * @returns {Number}
     */
    var getEndLocation = function ( anchor, headerHeight, offset ) {
        var location = 0;
        if (anchor.offsetParent) {
            do {
                location += anchor.offsetTop;
                anchor = anchor.offsetParent;
            } while (anchor);
        }
        location = location - headerHeight - offset;
        return location >= 0 ? location : 0;
    };

    /**
     * Determine the document's height
     * @private
     * @returns {Number}
     */
    var getDocumentHeight = function () {
        return Math.max(
            document.body.scrollHeight, document.documentElement.scrollHeight,
            document.body.offsetHeight, document.documentElement.offsetHeight,
            document.body.clientHeight, document.documentElement.clientHeight
        );
    };

    /**
     * Remove whitespace from a string
     * @private
     * @param {String} string
     * @returns {String}
     */
    var trim = function ( string ) {
        return string.replace(/^\s+|\s+$/g, '');
    };

    /**
     * Convert data-options attribute into an object of key/value pairs
     * @private
     * @param {String} options Link-specific options as a data attribute string
     * @returns {Object}
     */
    var getDataOptions = function ( options ) {
        var settings = {};
        // Create a key/value pair for each setting
        if ( options ) {
            options = options.split(';');
            options.forEach( function(option) {
                option = trim(option);
                if ( option !== '' ) {
                    option = option.split(':');
                    settings[option[0]] = trim(option[1]);
                }
            });
        }
        return settings;
    };

    /**
     * Update the URL
     * @private
     * @param {Element} anchor The element to scroll to
     * @param {Boolean} url Whether or not to update the URL history
     */
    var updateUrl = function ( anchor, url ) {
        if ( history.pushState && (url || url === 'true') ) {
            history.pushState( {
                pos: anchor.id
            }, '', anchor );
        }
    };

    /**
     * Start/stop the scrolling animation
     * @public
     * @param {Element} toggle The element that toggled the scroll event
     * @param {Element} anchor The element to scroll to
     * @param {Object} settings
     * @param {Event} event
     */
    exports.animateScroll = function ( toggle, anchor, options, event ) {

        // Options and overrides
        var settings = extend( settings || defaults, options || {} );  // Merge user options with defaults
        var overrides = getDataOptions( toggle ? toggle.getAttribute('data-options') : null );
        settings = extend( settings, overrides );

        // Selectors and variables
        var fixedHeader = document.querySelector('[data-scroll-header]'); // Get the fixed header
        var headerHeight = fixedHeader === null ? 0 : (fixedHeader.offsetHeight + fixedHeader.offsetTop); // Get the height of a fixed header if one exists
        var startLocation = root.pageYOffset; // Current location on the page
        var endLocation = getEndLocation( document.querySelector(anchor), headerHeight, parseInt(settings.offset, 10) ); // Scroll to location
        var animationInterval; // interval timer
        var distance = endLocation - startLocation; // distance to travel
        var documentHeight = getDocumentHeight();
        var timeLapsed = 0;
        var percentage, position;

        // Prevent default click event
        if ( toggle && toggle.tagName.toLowerCase() === 'a' && event ) {
            event.preventDefault();
        }

        // Update URL
        updateUrl(anchor, settings.updateURL);

        /**
         * Stop the scroll animation when it reaches its target (or the bottom/top of page)
         * @private
         * @param {Number} position Current position on the page
         * @param {Number} endLocation Scroll to location
         * @param {Number} animationInterval How much to scroll on this loop
         */
        var stopAnimateScroll = function (position, endLocation, animationInterval) {
            var currentLocation = root.pageYOffset;
            if ( position == endLocation || currentLocation == endLocation || ( (root.innerHeight + currentLocation) >= documentHeight ) ) {
                clearInterval(animationInterval);
                settings.callbackAfter( toggle, anchor ); // Run callbacks after animation complete
            }
        };

        /**
         * Loop scrolling animation
         * @private
         */
        var loopAnimateScroll = function () {
            timeLapsed += 16;
            percentage = ( timeLapsed / parseInt(settings.speed, 10) );
            percentage = ( percentage > 1 ) ? 1 : percentage;
            position = startLocation + ( distance * easingPattern(settings.easing, percentage) );
            root.scrollTo( 0, Math.floor(position) );
            stopAnimateScroll(position, endLocation, animationInterval);
        };

        /**
         * Set interval timer
         * @private
         */
        var startAnimateScroll = function () {
            settings.callbackBefore( toggle, anchor ); // Run callbacks before animating scroll
            animationInterval = setInterval(loopAnimateScroll, 16);
        };

        /**
         * Reset position to fix weird iOS bug
         * @link https://github.com/cferdinandi/smooth-scroll/issues/45
         */
        if ( root.pageYOffset === 0 ) {
            root.scrollTo( 0, 0 );
        }

        // Start scrolling animation
        startAnimateScroll();

    };

    /**
     * Initialize Smooth Scroll
     * @public
     * @param {Object} options User settings
     */
    exports.init = function ( options ) {

        // feature test
        if ( !supports ) return;

        // Selectors and variables
        settings = extend( defaults, options || {} ); // Merge user options with defaults
        var toggles = document.querySelectorAll('[data-scroll]'); // Get smooth scroll toggles

        // When a toggle is clicked, run the click handler
        forEach(toggles, function (toggle) {
            toggle.addEventListener('click', exports.animateScroll.bind( null, toggle, toggle.hash, settings ), false);
        });

    };


    //
    // Public APIs
    //

    return exports;

});
