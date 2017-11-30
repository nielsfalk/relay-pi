function pin(name, onOff) {
    var pinNumber = name.replace("pin","")
    $.ajax({ type: "POST", url: "/pin/" + pinNumber+"?on="+onOff })
}

function allPins(onOff){
    $.ajax({ type: "POST", url: "/all?on="+onOff })
}

function pattern(name){
    var speed = $(".speed").val()
    var direction = $('input[name=direction]:checked').val()
    $.ajax({ type: "POST", url: "/"+name+"?speed="+speed+(!direction ? "" : "&direction=" + direction) })
}