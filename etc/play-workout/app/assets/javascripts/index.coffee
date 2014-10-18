$ ->
  $.get "/users", (data) ->
    $.each data, (index, user) ->
      $('#users').append $('<li>').text user.name