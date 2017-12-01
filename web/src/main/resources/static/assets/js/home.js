$(document).ready(function () {

    $("#file1").change(function (){
        if ($("#file2").prop("value") != '') {
            $("#compare-button").prop("disabled", false);
        }
        $("#file1").blur();
    });
    $("#file2").change(function () {
        if ($("#file1").prop("value") != '') {
            $("#compare-button").prop("disabled", false);
        }
        $(this).blur();
    });

    $("#compare-button").click(function (event) {

        if ($("#compare-button").prop("disabled") == true) {
            return;
        }

        if (document.getElementById("summary-table-left") != null) {
            $("#reconciliation-result").animate({duration: 50, opacity: 0});
        }

        if ($("#file1").prop('value') == $("#file2").prop('value')) {
            $.notify("Warning: The same file has been selected twice. Please make sure you select 2 different files.", {
                className: "warn",
                position: "top left",
                autoHideDelay: 8000
            });
            return false;
        }

        //dualring animation
        var elem = $(event.currentTarget);
        elem.addClass('active');

        //stop submit the form, we will post it manually.
        event.preventDefault();

        // Get form
        var form = $('#compare-files-form')[0];

        // Create a FormData object
        var data = new FormData(form);

        // disable the submit button
        $("#compare-button").prop("disabled", true);
        $.ajax({
            type: "POST",
            enctype: 'multipart/form-data',
            url: "/",
            data: data,
            processData: false,
            contentType: false,
            cache: false,
            timeout: 60000, //60 secs
            success: function (data) {
                $("#reconciliation-result").html(data);
                $("#compare-button").prop("disabled", false);
                document.getElementById("reconciliation-result").style.visibility = "block";
                $("#reconciliation-result").animate({duration: 400, opacity: 1});
            },
            error: function (response, status) {
                if (status == "timeout") {
                    console.log("timeout");
                    $("#compare-button").prop("disabled", false);
                    $.notify("The CSV processing is either taking to long or not working properly. Please try again later.", {
                        className: "warn",
                        position: "top left",
                        autoHideDelay: 8000
                    });
                } else {
                    $("#compare-button").prop("disabled", false);
                    $.notify("Warning: " + response.responseJSON.message, {
                        className: "warn",
                        position: "top left",
                        autoHideDelay: 8000
                    });
                    console.log("Warning: " + response.responseJSON.message)
                }
            }
        }).always(function () {
            elem.removeClass('active');
        });
    });
});

window.onkeyup = function(e) {
    var key = e.keyCode ? e.keyCode : e.which;
    if (key == 49) {
        $("#file1").click()
    } else if (key == 50) {
        $("#file2").click()
    } else if (key == 13) {
        $("#compare-button").click()
    }
};

