'use strict';

function imageReset() {
    simpleDrawingBoard.reset({background: true});
    document.getElementById("prediction-container").innerHTML = "";

}

function imagePredictAndRender() {
    const server = window.location.href;
    const imageB64 = simpleDrawingBoard.getImg();
    const form = new FormData();
    form.append("data", imageB64);
    form.append("name", "drawn-" + dateFormatted());
    const settings1 = {
        "async": true,
        "crossDomain": false,
        "url": server + "predict",
        "method": "POST",
        "processData": false,
        "contentType": false,
        "mimeType": "multipart/form-data",
        "data": form
    };
    const settings2 = {
        "async": true,
        "crossDomain": false,
        "url": server + "preview",
        "method": "POST",
        "processData": false,
        "contentType": false,
        "mimeType": "multipart/form-data",
        "data": form
    };
    $.ajax(settings1).done(function (response) {
        console.log("Prediction:" + response);
        document.getElementById("prediction-container").innerHTML = "Prediction:" + response;
    });
    $.ajax(settings2).done(function (response) {
        // console.log( + response);
        const image = new Image();
        image.src = "data:image/png;base64," + response;
        document.getElementById("re-render").appendChild(image);
    });

}

function dateFormatted() {
    const date = new Date();
    return date.getFullYear() + "-"
        + date.getMonth() + "-"
        + date.getDate() + "-"
        + date.getHours() + "."
        + date.getMinutes() + "."
        + date.getSeconds()

}