$(function() {
   $('#console')
       .on('refresh', function() {
           this.src = './image?_cb=' + new Date().getTime();
       })
       .on('click', function(e) {
           $.ajax({
               url: 'click',
               data: {
                   x: e.offsetX,
                   y: e.offsetY
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
                .delay(500)
                .queue(function() {
                    $(this).trigger('refresh');
                });
       })
       .trigger('refresh');

});