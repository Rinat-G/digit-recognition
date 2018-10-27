'use strict';

function imageDownload() {
    let img = simpleDrawingBoard.getImg();
    console.log(img);
    img = img.replace("image/png", "application/octet-stream");
    console.log(img);
    // window.location.href = img;
    // window.open(img)

    let link = document.createElement('a');
    link.download = 'Image.png';
    link.href = img;
    link.click();
}

function imageReset() {
    simpleDrawingBoard.reset({background: true});
    document.getElementById("prediction-container").innerHTML = "";

}

function imagePredict() {
    const image = simpleDrawingBoard.getImg();
    console.log(image);

    const form = new FormData();
    form.append("data", image);
    form.append("name", "drawn-" + dateFormatted());

    const settings = {
        "async": true,
        "crossDomain": false,
        "url": "http://localhost:8080/predict",
        "method": "POST",
        "processData": false,
        "contentType": false,
        "mimeType": "multipart/form-data",
        "data": form
    };

    $.ajax(settings).done(function (response) {
        console.log("Prediction:" + response);
        document.getElementById("prediction-container").innerHTML = "Prediction:" + response;
    });
}

function imageReRender() {

    const imageB64 = simpleDrawingBoard.getImg();
    // const image = new Image();
    // image.src = imageB64;
    // document.getElementById("re-render").appendChild(image);

    const form = new FormData();
    form.append("data", imageB64);
    form.append("name", "drawn-" + dateFormatted());

    const settings = {
        "async": true,
        "crossDomain": false,
        "url": "http://localhost:8080/preview",
        "method": "POST",
        "processData": false,
        "contentType": false,
        "mimeType": "multipart/form-data",
        "data": form
    };

    $.ajax(settings).done(function (response) {
        // console.log( + response);
        const image = new Image();
        image.src = "data:image/png;base64," + response;
        document.getElementById("re-render").appendChild(image);
    });


}

function imagePredictAndRender() {
    const imageB64 = simpleDrawingBoard.getImg();
    const form = new FormData();
    form.append("data", imageB64);
    form.append("name", "drawn-" + dateFormatted());
    const settings1 = {
        "async": true,
        "crossDomain": false,
        "url": "http://localhost:8080/predict",
        "method": "POST",
        "processData": false,
        "contentType": false,
        "mimeType": "multipart/form-data",
        "data": form
    };
    const settings2 = {
        "async": true,
        "crossDomain": false,
        "url": "http://localhost:8080/preview",
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