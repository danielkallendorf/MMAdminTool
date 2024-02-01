const cal_list = document.getElementById("search");
const cal_frame = document.getElementById("cal_frame");
const input_field = document.getElementById("input")
var cal_map = {};

const xhttp = new XMLHttpRequest();
xhttp.onload = function () {
    cal_map = JSON.parse(this.responseText);
    for (var name of Object.keys(cal_map)) {
        if(name == "Week Numbers"){
            continue;
        }
        var newOptionElement = document.createElement("option");
        newOptionElement.textContent = name;
        cal_list.appendChild(newOptionElement);
    }
}
xhttp.open("GET", "/mmplan/calendars.json", true);
xhttp.send();


function makeSRC(cal_id) {
    if (!cal_id) {
        cal_id = "bWVzc2RpZW5lci5tdWVuc3RlckBnbWFpbC5jb20";
        showTitle = 0;
    } else {
        showTitle = 1;
    }
    var src = `https://calendar.google.com/calendar/embed?height=600&wkst=2&bgcolor=%23ffffff&ctz=Europe%2FBerlin&showPrint=0&showCalendars=0&mode=AGENDA&showDate=0&showTitle=${showTitle}&src=${cal_id}&color=%237986CB`;
    return src;
}

function setIframe(cal_id) {
    cal_frame.src = makeSRC(cal_id);
}

function onInput() {
    var val = input_field.value;
    if (val == "") {
        setIframe("");
        return;
    }

    var cal_id = cal_map[val];
    if (cal_id) {
        setIframe(cal_id);
    }
}