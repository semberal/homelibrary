$(document).ready(function() {
    $('form.google-books-fetch button').click(function() {

        var foundText = $(this).data("loading-found");
        var notFoundText = $(this).data("loading-not-found");


        $.ajax({
            url: $('form.google-books-fetch').data('gb-url'),
            type: 'get',
            data: {"isbn" : $('form.google-books-fetch input').val()},
            beforeSend: function() {
                $('form.google-books-fetch button').button("loading");
            }
        }).done(function(data) {
            $('form.google-books-fetch span.fetch-result').text(foundText);
            $('div.add-book-form-container').html(data);
            initTooltips();
        }).fail(function() {
            $('form.google-books-fetch span.fetch-result').text(notFoundText);
        }).always(function() {
            $('form.google-books-fetch button').button("reset");
        });


        return false;
   });
});