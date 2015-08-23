$(function() {
   $('#console')
       .on('refresh', function() {
           var params = $(this).data();
           params.dummy = new Date().getTime();
           this.src = './image?' + $.param(params);
       })
       .on('click', function(e) {
           var scale = $(this).data('scale') / 100;
            $.ajax({
                url: 'click',
                data: {
                    x: Math.floor(e.offsetX / scale),
                    y: Math.floor(e.offsetY / scale)
                },
                dataType: 'json',
                method: 'GET'
            }).done(function() {
                $(this).trigger('refresh');
            });
        })
        .load(function() {
             $(this)
                 .stop()
                 .delay(1000)
                 .queue(function() {
                     $(this).trigger('refresh');
                 });
        })
        .data({
            scale: 100,
            quality: 80
        })
        .trigger('refresh'
    );
       
    $('#full_size')
        .on('click', function() {
            $('#flexible_form').hide();
            init_menu();
            var flash_height = 580;
            var flash_width = 960;
            var scale = 0;
            if ($(window).height() > $(window).width()) {
                    scale = $(window).width() / flash_width;
            } else {
                    scale = $(window).height() / flash_height;                    
            }
            $('#console')
                .data('scale', Math.floor(scale * 100))
                .trigger('refresh');
        return false;
    });
                        
    $('#flexible_size')
        .on('click', function() {
            $('#flexible_form .scale').val($('#console').data('scale'));
            $('#flexible_form').show();
    });
               
    $('#flexible_form')
        .on('submit', function() {
            $('#console')
                .data('scale', $('.scale', this).val())
                .trigger('refresh');
            $(this).hide();
            init_menu();
            return false;
    });

   $('#menu').hide();
   $('#menubar').on('click', function() {
       $('#menubar').css('width', "auto");
       $('#menu').show();
   });
   
   function init_menu() {
       $('#menu').hide();
       $('#menubar').css('width', '1em');
   }
});