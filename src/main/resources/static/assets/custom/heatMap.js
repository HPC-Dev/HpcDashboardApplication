var BACKGROUND_COLORS = ['rgb(19,91,105)', 'rgb(133,155,163)', 'rgb(20,116,132)', '#8DB9CA', 'rgb(173,183,191)', 'rgb(21,104,121)', 'rgba(255, 99, 132, 0.2)', 'rgba(75, 192, 192, 0.2)', 'rgba(255, 206, 86, 0.2)', 'rgba(153, 102, 255, 0.2)', '#EFEBE9', 'rgba(54, 162, 235, 0.2)', 'rgba(192, 0, 0, 0.2)', '#D1C4E9', '#BBDEFB', '#FFD180', , '#90A4AE', '#F9A825', '#C5E1A5', '#80CBC4', '#7986CB', '#7E57C2', '#3949AB', '#e57373', '#546E7A', '#A1887F'];

Chart.defaults.global.defaultFontStyle = 'bold';
Chart.defaults.global.defaultFontFamily = 'Verdana';

var app;
var cpuList = [];
var typeList = [];
var workloads = [];
var cpuData = {};

function clearHtml() {
    $('#heading').empty();
    $('#footnote').hide();
    $('.collapse').collapse('hide')

}

window.onload = function() {

    $.getJSON("/workloads", {
        ajax: 'true'
    }, function(data) {
        var len = data.length;

        if (len > 1) {

            $("#workloadCheckBox").show();
        } else {

            $("#workloadCheckBox").hide();
        }
        var html = '';

        for (var i = 0; i < len; i++) {

            html += ' <div id="workloadCheckBox" class="custom-control custom-checkbox custom-control-inline">';

            html += '<input class="custom-control-input" type="checkbox" name="type" id="' + data[i] + '" value="' + data[i] + ' "   onchange="workloadCheckBoxChange(\'' + data[i] + '\')" />' +
                '<label class="custom-control-label" text="' + data[i] + '" for="' + data[i] + '" >' + data[i] + '</label>';


            html += '</div>';
        }
        $('#workloadCheckBox').append(html);

    });

    cpuDropDownLoad();

};


function workloadCheckBoxChange(workload) {

    if (workload) {

        var index = workloads.indexOf(workload);
        if (index > -1) {
            workloads.splice(index, 1);
        } else {
            workloads.push(workload);
        }
    }

    cpuDropDownLoad();
}

function cpuDropDownLoad() {

    var params = {};
    params.workloads = workloads;
    var preCpu1 = $("#cpuDrop1 option:selected").val();
    var preCpu2 = $("#cpuDrop2 option:selected").val();
    var preCpu3 = $("#cpuDrop3 option:selected").val();
    var preCpu4 = $("#cpuDrop4 option:selected").val();


    $.getJSON("/cpusByWorkload", $.param(params, true), function(data) {

        var html = '<option value="" selected="true" disabled="disabled">-- CPU1 --</option>';

        cpuData ={};
        for (var cpuGen in data) {

            cpuData[cpuGen] = data[cpuGen];
        }


        for (var cpuGen in data) {

            html += '<optgroup label="' + cpuGen + '">'

            for (var cpu in data[cpuGen]) {
                html += '<option value="' + data[cpuGen][cpu] + '">' +
                    data[cpuGen][cpu] + '</option>';
            }

            html += '</optgroup>'
        }

        $('#cpuDrop1').html(html);

        for (var cpuGen in data) {

            if (data[cpuGen].includes(preCpu1)) {
                $('#cpuDrop1').val(preCpu1);
                cpuChange1();
                break;
            } else {
                $('#cpuDrop1').val('');
                $('#typeDrop1').val('');
                clearHtml();
            }

        }

        var flag = 0;
            for (var cpu in cpuList) {

                    for (var cpuGen in cpuData) {

                        if (cpuData[cpuGen].includes(cpuList[cpu])) {
                            flag = 1;
                            break;
                        }
                    }
                }
                if (flag == 0) {
                    $('#tableHeatMap').html('');
                }



    });

    $.getJSON("/cpusByWorkload", $.param(params, true), function(data) {

        var html = '<option value="" selected="true" disabled="disabled">-- CPU2 --</option>';

        for (var cpuGen in data) {

            html += '<optgroup label="' + cpuGen + '">'

            for (var cpu in data[cpuGen]) {
                html += '<option value="' + data[cpuGen][cpu] + '">' +
                    data[cpuGen][cpu] + '</option>';

            }

            html += '</optgroup>'
        }

        $('#cpuDrop2').html(html);

        for (var cpuGen in data) {

            if (data[cpuGen].includes(preCpu2)) {
                $('#cpuDrop2').val(preCpu2);
                cpuChange2();
                break;
            } else {
                $('#cpuDrop2').val('');
                $('#typeDrop2').val('');
                clearHtml();

            }

        }


    });

    $.getJSON("/cpusByWorkload", $.param(params, true), function(data) {

        var html = '<option value="" selected="true" disabled="disabled">-- CPU3 --</option>';

        for (var cpuGen in data) {

            html += '<optgroup label="' + cpuGen + '">'

            for (var cpu in data[cpuGen]) {
                html += '<option value="' + data[cpuGen][cpu] + '">' +
                    data[cpuGen][cpu] + '</option>';

            }

            html += '</optgroup>'
        }

        $('#cpuDrop3').html(html);

        for (var cpuGen in data) {

            if (data[cpuGen].includes(preCpu3)) {
                $('#cpuDrop3').val(preCpu3);
                cpuChange3();
                break;
            } else {
                $('#cpuDrop3').val('');
                $('#typeDrop3').val('');
                clearHtml();

            }

        }


    });

    $.getJSON("/cpusByWorkload", $.param(params, true), function(data) {

        var html = '<option value="" selected="true" disabled="disabled">-- CPU4 --</option>';

        for (var cpuGen in data) {

            html += '<optgroup label="' + cpuGen + '">'

            for (var cpu in data[cpuGen]) {
                html += '<option value="' + data[cpuGen][cpu] + '">' +
                    data[cpuGen][cpu] + '</option>';

            }

            html += '</optgroup>'
        }

        $('#cpuDrop4').html(html);

        for (var cpuGen in data) {

            if (data[cpuGen].includes(preCpu4)) {
                $('#cpuDrop4').val(preCpu4);
                cpuChange4();
                break;
            } else {
                $('#cpuDrop4').val('');
                $('#typeDrop4').val('');
                clearHtml();

            }

        }
    });


}

$("#cpuDrop1").on("change", cpuChange1);
$("#cpuDrop2").on("change", cpuChange2);
$("#cpuDrop3").on("change", cpuChange3);
$("#cpuDrop4").on("change", cpuChange4);

function cpuChange1() {

    var value = $('#cpuDrop1').val();

    var preValue1 = $("#typeDrop1 option:selected").val();

    clearHtml();
    $('#tableHeatMap').html('');
    if (value != '') {
        $.getJSON("/runTypesByCPU", {
            cpu: value,
            ajax: 'true'
        }, function(data) {

            var html = '<option value="" selected="true" disabled="disabled">-- RunType1 --</option>';
            var len = data.length;

            for (var i = 0; i < len; i++) {
                html += '<option value="' + data[i] + '">' +
                    data[i] + '</option>';
            }
            html += '</option>';
            $('#typeDrop1').html(html);

            if (data.includes(preValue1)) {
                $('#typeDrop1').val(preValue1);
            } else if (data.includes('latest')) {
                $('#typeDrop1').val('latest');
            } else {
                $('#typeDrop1').val('');
            }
            getISV();

            setTimeout(getData, 40);
        });
    }

}


function cpuChange2() {
    var value = $('#cpuDrop2').val();
    var preValue2 = $("#typeDrop2 option:selected").val();

    clearHtml();
    $('#tableHeatMap').html('');
    if (value != '') {
        $.getJSON("/runTypesByCPU", {
            cpu: value,
            ajax: 'true'
        }, function(data) {
            var html = '<option value="" selected="true" disabled="disabled">-- RunType2 --</option>';
            var len = data.length;

            for (var i = 0; i < len; i++) {
                html += '<option value="' + data[i] + '">' +
                    data[i] + '</option>';
            }
            html += '</option>';
            $('#typeDrop2').html(html);

            if (data.includes(preValue2)) {
                $('#typeDrop2').val(preValue2);
            } else if (data.includes('latest')) {
                $('#typeDrop2').val('latest');
            } else {
                $('#typeDrop2').val('');
            }

            getISV();

            setTimeout(getData, 40);

        });

    }
}


function cpuChange3() {
    var value = $('#cpuDrop3').val();
    var preValue3 = $("#typeDrop3 option:selected").val();
    clearHtml();
    $('#tableHeatMap').html('');
    if (value != '') {
        $.getJSON("/runTypesByCPU", {
            cpu: value,
            ajax: 'true'
        }, function(data) {
            var html = '<option value="" selected="true" disabled="disabled">-- RunType3 --</option>';
            var len = data.length;

            for (var i = 0; i < len; i++) {
                html += '<option value="' + data[i] + '">' +
                    data[i] + '</option>';
            }
            html += '</option>';
            $('#typeDrop3').html(html);

            if (data.includes(preValue3)) {
                $('#typeDrop3').val(preValue3);
            } else if (data.includes('latest')) {

                $('#typeDrop3').val('latest');
            } else {
                $('#typeDrop3').val('');
            }
            getISV();

            setTimeout(getData, 40);
        });

    }


}


function cpuChange4() {
    var value = $('#cpuDrop4').val();
    var preValue4 = $("#typeDrop4 option:selected").val();
    clearHtml();
    $('#tableHeatMap').html('');
    if (value != '') {
        $.getJSON("/runTypesByCPU", {
            cpu: value,
            ajax: 'true'
        }, function(data) {
            var html = '<option value="" selected="true" disabled="disabled">-- RunType4 --</option>';
            var len = data.length;

            for (var i = 0; i < len; i++) {
                html += '<option value="' + data[i] + '">' +
                    data[i] + '</option>';
            }
            html += '</option>';
            $('#typeDrop4').html(html);

            if (data.includes(preValue4)) {
                $('#typeDrop4').val(preValue4);
            } else if (data.includes('latest')) {

                $('#typeDrop4').val('latest');
            } else {
                $('#typeDrop4').val('');
            }
            getISV();

            setTimeout(getData, 40);
        });

    }


}


$('#type1').on("change", function() {

    getISV();

    setTimeout(getData, 40);

});

$('#type2').on("change", function() {

    getISV();

    setTimeout(getData, 40);

});

$('#type3').on("change", function() {

    getISV();

    setTimeout(getData, 40);

});

$('#type4').on("change", function() {

    getISV();

    setTimeout(getData, 40);

});


$("#isv").on("change", getData);


function captureCPUsTypes() {

    var params = {};
    params.workloads = workloads;
    cpuList = [];
    typeList = [];

    cpu1 = $('#cpuDrop1')[0].value;
    cpu2 = $('#cpuDrop2')[0].value;
    cpu3 = $('#cpuDrop3')[0].value;
    cpu4 = $('#cpuDrop4')[0].value;


    type1 = $('#typeDrop1')[0].value;
    type2 = $('#typeDrop2')[0].value;
    type3 = $('#typeDrop3')[0].value;
    type4 = $('#typeDrop4')[0].value;

    cpuList.push(cpu1);
    cpuList.push(cpu2);
    cpuList.push(cpu3);
    cpuList.push(cpu4);

    typeList.push(type1);
    typeList.push(type2);
    typeList.push(type3);
    typeList.push(type4);


     var filteredTypeList = typeList.filter(function(type) {
            return type != "";
        });

        typeList = filteredTypeList;

        for (var i = 0; i < cpuList.length; i++) {
            if (cpuList[i].includes("CPU") || cpuList[i] == "") {
                cpuList.splice(i, 1);
                i--;
            }
        }

    for (var cpu in cpuList) {
        var flag = 0;
        for (var cpuGen in cpuData) {

            if (cpuData[cpuGen].includes(cpuList[cpu])) {
                flag = 1;
                break;
            }
        }

        if (flag == 0) {
            const index = cpuList.indexOf(cpuList[cpu])
            cpuList.splice(index, 1);
            typeList.splice(index, 1);
        }
    }


}


function getISV() {

    captureCPUsTypes();
    if ((cpuList.length > 1 && typeList.length > 1 && (cpuList.length == typeList.length)) && !(cpu1 === cpu2 && type1 === type2)) {

        var params = {};
        params.cpuList = cpuList;
        params.typeList = typeList;
        params.workloads = workloads;

        var preISV = $("#isvDrop option:selected").val();

        $.getJSON("/avg/isvDrop/", $.param(params, true), function(data) {
            var html = '<option value="All" selected="true" >All</option>';
            var len = data.length;

            for (var i = 0; i < len; i++) {
                html += '<option value="' + data[i] + '">' +
                    data[i] + '</option>';
            }
            html += '</option>';
            $('#isvDrop').html(html);

            if (data.includes(preISV)) {
                $('#isvDrop').val(preISV);
            } else {

                $('#isvDrop').val('All');
            }
        });
    }

}


function getData() {
    clearHtml();
    $('#tableHeatMap').html('');

    captureCPUsTypes();

    if ((cpuList.length > 1 && typeList.length > 1 && (cpuList.length == typeList.length)) && !(cpu1 === cpu2 && type1 === type2)) {

        var isv = $('#isvDrop')[0].value;


        var params = {};
        params.cpuList = cpuList;
        params.typeList = typeList;
        params.isv = isv;


        if (cpuList.length > 3)
            $('head').append('<link rel="stylesheet" href="assets/custom/heatMap.css"/>');

        $.getJSON("/avg/heatMap/", $.param(params, true), function(data) {
            if (data.heatMapResults != null && data.heatMapResults.length > 1) {
                updateTable(data.columns, data.heatMapResults);
            } else {
                table = "<p>No data available</p>";
                $('#tableHeatMap').html(table);
            }
        });
    } else {
        clearHtml();
        $('#tableHeatMap').html('');
    }

}


function updateTable(columns, data, comment) {

    var header = "";
    header += cpuList[0] + "_" + typeList[0] + " vs ( ";

    for (i = 1; i < cpuList.length; i++) {
        header += cpuList[i] + "_" + typeList[i];
        if (i + 1 != cpuList.length)
            header += ", ";
    }
    header += " ) ";

    var table;
    if (Object.keys(data).length > 0) {
        $('#heading').empty();
        var heading = "<p style='font-weight: bold;font-size:15px;text-align:left;font-family:verdana;'>" + header + "</p>"
        $('#heading').append(heading);
        $('#heading').show();
        $('#footnote').show();

        table = "<table class='table table-responsive' >" + getHeaders(columns) + getBody(columns, data) + "</table>";


    } else {
        clearHtml();
        table = "<p>No data available</p>";
    }

    $('#tableHeatMap').html(table);
}

function getHeaders(columns) {
    var headers = ['<thead><tr>'];
    columns.forEach(function(column) {

        if (column === 'isv') {
            headers.push('<th style="border-bottom: 1px solid black;border-collapse: collapse"; bgcolor="#343A40"> <font color="white">' + column.toUpperCase() + '</font></th>')
        } else if (column === 'perNode1' || column === 'perCore1') {
            headers.push('<th style="border-bottom: 1px solid black;border-collapse: collapse"; bgcolor="#343A40"> <font color="white"> ' + cpuList[1] + '</br>' + "/ " + column.slice(3, -1) + '</font></th>')
        } else if (column === 'perNode2' || column === 'perCore2') {
            headers.push('<th style="border-bottom: 1px solid black;border-collapse: collapse"; bgcolor="#343A40"> <font color="white"> ' + cpuList[2] + '</br>' + "/ " + column.slice(3, -1) + '</font></th>')
        } else if (column === 'perNode3' || column === 'perCore3') {
            headers.push('<th style="border-bottom: 1px solid black;border-collapse: collapse"; bgcolor="#343A40"> <font color="white"> ' + cpuList[3] + '</br>' + "/ " + column.slice(3, -1) + '</font></th>')
        } else if (column === 'perDollar1') {
            headers.push('<th style="border-bottom: 1px solid black;border-collapse: collapse"; bgcolor="#343A40"> <font color="white"> ' + cpuList[1] + '</br>' + "/ $" + '</font></th>')
        } else if (column === 'perDollar2') {
            headers.push('<th style="border-bottom: 1px solid black;border-collapse: collapse"; bgcolor="#343A40"> <font color="white"> ' + cpuList[2] + '</br>' + "/ $" + '</font></th>')
        } else if (column === 'perDollar3') {
            headers.push('<th style="border-bottom: 1px solid black;border-collapse: collapse"; bgcolor="#343A40"> <font color="white"> ' + cpuList[3] + '</br>' + "/ $" + '</font></th>')
        } else if (column === 'perWatt1') {
            headers.push('<th style="border-bottom: 1px solid black;border-collapse: collapse"; bgcolor="#343A40"> <font color="white"> ' + cpuList[1] + '</br>' + "/ Watt" + '</font></th>')
        } else if (column === 'perWatt2') {
            headers.push('<th style="border-bottom: 1px solid black;border-collapse: collapse"; bgcolor="#343A40"> <font color="white"> ' + cpuList[2] + '</br>' + "/ Watt" + '</font></th>')
        } else if (column === 'perWatt3') {
            headers.push('<th style="border-bottom: 1px solid black;border-collapse: collapse"; bgcolor="#343A40"> <font color="white"> ' + cpuList[3] + '</br>' + "/ Watt" + '</font></th>')
        } else {
            headers.push('<th style="border-bottom: 1px solid black;border-collapse: collapse"; bgcolor="#343A40"> <font color="white">' + column.charAt(0).toUpperCase() + column.slice(1) + '</font></th>')
        }
    });
    headers.push('</tr></thead>');

    return headers.join('');
}

function getFooters(columns) {
    var footers = ['<tfoot><tr>'];
    columns.forEach(function(column) {

        if (column === 'isv') {
            footers.push('<th style="border-bottom: 1px solid black;border-collapse: collapse"; bgcolor="#343A40"> <font color="white">' + column.toUpperCase() + '</font></th>')
        } else if (column === 'perNode1' || column === 'perCore1' || column === 'perDollar1' || column === 'perWatt1') {
            footers.push('<th style="border-bottom: 1px solid black;border-collapse: collapse"; bgcolor="#343A40"> <font color="white"> ' + cpuList[1] + '</br>' + column.charAt(0).toUpperCase() + column.slice(3, -1) + '</font></th>')
        } else if (column === 'perNode2' || column === 'perCore2' || column === 'perDollar2' || column === 'perWatt2') {
            footers.push('<th style="border-bottom: 1px solid black;border-collapse: collapse"; bgcolor="#343A40"> <font color="white"> ' + cpuList[2] + '</br>' + column.charAt(0).toUpperCase() + column.slice(3, -1) + '</font></th>')
        } else if (column === 'perNode3' || column === 'perCore3' || column === 'perDollar3' || column === 'perWatt3') {
            footers.push('<th style="border-bottom: 1px solid black;border-collapse: collapse"; bgcolor="#343A40"> <font color="white"> ' + cpuList[3] + '</br>' + column.charAt(0).toUpperCase() + column.slice(3, -1) + '</font></th>')
        } else {
            footers.push('<th style="border-bottom: 1px solid black;border-collapse: collapse"; bgcolor="#343A40"> <font color="white">' + column.charAt(0).toUpperCase() + column.slice(1) + '</font></th>')
        }


    });
    footers.push('</tr></tfoot>');

    return footers.join('');
}

function getBody(columns, data) {
    var body = ['<tbody>'];
    data.forEach(function(row) {
        body.push(generateRow(columns, row))
    });
    body.push('</tbody>');

    return body.join('');
}

function generateRow(columns, rowData) {
    var row = ['<tr style="border: 1px solid black;border-collapse: collapse";>'];
    var val;
    var flag = 0;

    columns.forEach(function(column) {
        if (rowData[column] != undefined) {
            val = rowData[column];
        } else {
            val = '';
        }

        if (column === 'category' && rowData[column] != null) {
            row = [];
            row = ['<tr style="border: 1px solid black;border-collapse: collapse"; bgcolor="#78909C">']
            flag = 1;
        } else if ((column === 'isv' && rowData[column] != null)) {
            row = [];
            row = ['<tr style="border: 1px solid black;border-collapse: collapse"; bgcolor="#ECEFF1"> <td bgcolor="white"></td>']
            flag = 2;
        }

        if (column != "") {

            if (flag == 1) {
                if (val.startsWith("+")) {
                    row.push('<td style="border: 1px solid black;border-collapse: collapse"; bgcolor="#C8E6C9">' + val.substring(1) + '</td>')
                } else if (val.startsWith("-")) {
                    row.push('<td style="border: 1px solid black;border-collapse: collapse"; bgcolor="FFCDD2">' + val + '</td>')
                } else {
                    row.push('<td style="border: 1px solid black;border-collapse: collapse";> <font color="white" font-weight: bold >' + val + '</font></td>')
                }

            } else if (flag == 2) {
                if (val.startsWith("+")) {
                    row.push('<td style="border: 1px solid black;border-collapse: collapse"; bgcolor="#ECEFF1"> <b>' + val.substring(1) + '</b></td>')
                } else if (val.startsWith("-")) {
                    row.push('<td style="border: 1px solid black;border-collapse: collapse"; bgcolor="ECEFF1"> <b>' + val + ' </b></td>')
                } else {
                    row.push('<td style="border: 1px solid black;border-collapse: collapse;"  > <b>' + val + '</b></td>')
                }

            } else {

                if (val != "") {
                    if (val.startsWith("+")) {
                        row.push('<td style="border: 1px solid black;border-collapse: collapse">' + val.substring(1) + '</td>')
                    } else {
                        row.push('<td style="border: 1px solid black;border-collapse: collapse" >' + val + '</td>')
                    }
                } else {
                    row.push('<td>' + val + '</td>')
                }
            }

        } else {
            row.push('<td bgcolor="#FFFFFF"  style="font-weight:bold ; white-space: nowrap">' + val + '</td>')

        }
    });

    row.push('</tr>');
    flag = 0;

    return row.join('');
}