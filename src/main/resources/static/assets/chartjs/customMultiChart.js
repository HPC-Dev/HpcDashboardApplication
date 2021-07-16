Chart.defaults.global.defaultFontStyle = 'bold';
Chart.defaults.global.defaultFontFamily = 'Verdana';

var typeVal;
var flag;
var cpuList = [];
var runTypes = [];

var store = {};

function addToStore(key, value) {
    store[key] = value;
}

function getFromStore(key) {
    return store[key];
}

function clearFromStore(key) {
    delete store[key];
}


$('#clearButton').on('click', function() {

    $('input[type=checkbox]').prop('checked', false);
    cpuList.length = 0;
    runTypes.length=0;
    clearChart();
//    $("#clear").hide();
    clearHtml();
});


$('#appDrop').on("change", function() {

    addToStore('runTypes',runTypes );
    addToStore('cpuList',cpuList );


    $('#checkbox').empty();
    $("#typeCheckBox").empty();
    var value = $(this).val();
    $('#footnote').hide();
    $("#p1").hide();
    $("#p2").hide();
    $('.collapse').collapse('hide')
    $.getJSON("/runTypes", {
        appName: value,
        ajax: 'true'
    }, function(data) {
        var len = data.length;

        if (len > 1) {
            flag = 1;
            $("#typeCheckBox").show();
        } else {
            flag = 0;
            typeVal = data[0];
            $("#typeCheckBox").hide();
        }
        runTypeCheckBoxChange();
        var html = '';
        var previousRunTypes = getFromStore('runTypes');
        var isAnyChecked = false;


        for (var i = 0; i < len; i++) {
            var isChecked = previousRunTypes.includes(data[i]);

            if(isChecked ){
                isAnyChecked = true;
            }


            html += ' <div id="typeCheckBox" class="custom-control custom-checkbox custom-control-inline">';
            html += '<input class="custom-control-input" type="checkbox" name="type" id="' + data[i] + '" value="' + data[i] + '" ' + (isChecked ? 'checked' : '') + '  onchange="runTypeCheckBoxChange()"/>' +
                '<label class="custom-control-label" text="' + data[i] + '" for="' + data[i] + '" >' + data[i] + '</label>';
            html += '</div>';
        }

        $('#typeCheckBox').append(html);

        if(isAnyChecked && flag ==1 ) {
            runTypeCheckBoxChange();
        }

    });

    clearChart();
    clearHtml();

});


function runTypeCheckBoxChange() {

    $('#checkbox').empty();
    $('#footnote').hide();
    $("#p1").hide();
    $("#p2").hide();
    $('.collapse').collapse('hide')
    var app = $('#appDrop')[0].value;
    var runTypes = [];
//    cpuList.length = 0;
    if (flag == 1) {
        $("#typeCheckBox input:checked").each(function() {
            runTypes.push($(this).val());
        });
    } else {
        runTypes.push(typeVal);
    }

    var params = {};
    params.runTypes = runTypes;

    if (runTypes.length >= 1) {
        $("#checkbox").show();
        $("#clear").show();
        $.getJSON("/cpusSelected/" + app, $.param(params, true), function(data) {
            var len = data.length;
            var html = '';
            var previousCpuList = getFromStore('cpuList');
            var isAnyChecked = false;
            for (var i = 0; i < len; i++) {
                var isChecked = previousCpuList.indexOf(data[i]);
                var bool =false;
                if(isChecked != -1){
                bool = true;
                }
                 if(bool) {
                isAnyChecked = true;
                }


                html += ' <div id="cpuCheckBox" class="custom-control custom-checkbox custom-control-inline">';
                html += '<input class="custom-control-input" type="checkbox" name="type" id="' + data[i] + '" value="' + data[i] + '" ' + (bool ? 'checked' : '') + ' onchange="checkBoxChange(\'' + data[i] + '\')"/>' +
                    '<label class="custom-control-label" text="' + data[i] + '" for="' + data[i] + '" >' + data[i] + '</label>';
                html += '</div>';
            }

            selectedItems = [];

            if(isAnyChecked) {
                selectedItems.push(previousCpuList)
            }

            $('#checkbox').append(html);

            selectedItems.forEach(item => checkBoxChange(item))

        });
    } else {
        $("#checkbox").hide();
        $("#clear").hide();
        $('#footnote').hide();
        $("#p1").hide();
        $("#p2").hide();
        $('.collapse').collapse('hide')
    }
    checkBoxChange();
}


function checkBoxChange(cpuType) {

     runTypes = [];

    if (cpuType) {

        const index = cpuList.indexOf(cpuType);
        if (index > -1) {
            cpuList.splice(index, 1);
        } else {
            cpuList.push(cpuType);
        }
    }

    if (flag === 1) {
        $("#typeCheckBox input:checked").each(function() {
            runTypes.push($(this).val());
        });
    } else {
        runTypes.push(typeVal);
    }

    if ((runTypes.length > 1 && cpuList.length >= 1) || (runTypes.length >= 1 && cpuList.length > 1)) {

        var params = {};
        params.cpuList = cpuList;

        var typeParams = {};
        params.runTypes = runTypes;

        if ($("#appDrop").val() === null) {
            console.log("empty");
        }

        getMultiChartData();
        //    var BACKGROUND_COLORS = ['rgba(255, 99, 132, 0.2)', 'rgba(75, 192, 192, 0.2)', 'rgba(255, 206, 86, 0.2)', 'rgba(153, 102, 255, 0.2)', 'rgba(255, 159, 64, 0.2)', 'rgba(54, 162, 235, 0.2)', 'rgba(192, 0, 0, 0.2)','#FF9E80', '#03A9F4', '#FFD180', , '#90A4AE', '#F9A825',  '#C5E1A5', '#80CBC4', '#7986CB', '#7E57C2', '#3949AB', '#e57373', '#546E7A', '#A1887F'];
        //    var BORDER_COLORS = ['rgba(255, 99, 132, 1)', 'rgba(75, 192, 192, 1)', 'rgba(255, 206, 86, 1)', 'rgba(153, 102, 255, 1)', 'rgba(255, 159, 64, 1)', 'rgba(54, 162, 235, 1)', 'rgba(192, 0, 0, 1)'];

        var BACKGROUND_COLORS = ['rgb(19,91,105)', 'rgb(133,155,163)', 'rgb(20,116,132)', '#8DB9CA', 'rgb(173,183,191)', 'rgb(21,104,121)', 'rgba(255, 99, 132, 0.2)', 'rgba(75, 192, 192, 0.2)', 'rgba(255, 206, 86, 0.2)', 'rgba(153, 102, 255, 0.2)', '#EFEBE9', 'rgba(54, 162, 235, 0.2)', 'rgba(192, 0, 0, 0.2)', '#D1C4E9', '#BBDEFB', '#FFD180', , '#90A4AE', '#F9A825', '#C5E1A5', '#80CBC4', '#7986CB', '#7E57C2', '#3949AB', '#e57373', '#546E7A', '#A1887F'];
        //var BORDER_COLORS = ['','','','','','','rgba(255, 99, 132, 1)', 'rgba(75, 192, 192, 1)', 'rgba(255, 206, 86, 1)', 'rgba(153, 102, 255, 1)', '', 'rgba(54, 162, 235, 1)', 'rgba(192, 0, 0, 1)'];

        function getMultiChartData() {

            getData(cpuList);
            var app = $('#appDrop')[0].value;
            if (app && (runTypes.length > 1 && cpuList.length >= 1) || (runTypes.length >= 1 && cpuList.length > 1)) {
                $.getJSON("/chart/multiCPUResult/" + app, $.param(params, true), function(data) {
                    var label = data.cpus;
                    if (data.dataset[0].value.length > 1) {
                        if (data.dataset.length > 1) {
                            var chartdata = {
                                labels: label,
                                datasets: data.dataset.map(function(dataset, index) {

                                    if (dataset.value.length > 2) {
                                        return {
                                            label: dataset.bmName,
                                            backgroundColor: BACKGROUND_COLORS[index],
                                            borderWidth: 1,
                                            data: dataset.value,
                                            fill: false
                                        };
                                    } else {
                                        return {
                                            label: dataset.bmName,
                                            backgroundColor: BACKGROUND_COLORS[index],
                                            borderWidth: 1,
                                            data: dataset.value,
                                            fill: false,
                                            barPercentage: 0.8,
                                        };
                                    }

                                })
                            };
                        } else {
                            var chartdata = {
                                labels: label,
                                datasets: data.dataset.map(function(dataset, index) {
                                    return {
                                        label: dataset.bmName,
                                        backgroundColor: BACKGROUND_COLORS[index],
                                        borderWidth: 1,
                                        data: dataset.value,
                                        fill: false,
                                        barPercentage: 0.4
                                    };
                                })
                            };
                        }

                        var chartOptions = {

                            legend: {
                                display: true,
                                position: 'right'
                            },

                            title: {
                                display: true,
                                text: data.appName
                            },
                            scales: {
                                yAxes: [{
                                    ticks: {
                                        beginAtZero: true,
                                    },
                                    scaleLabel: {
                                        display: true,
                                        labelString: 'Relative Performance'
                                    },
                                    gridLines: {
                                        color: "rgb(234, 236, 244)",
                                        zeroLineColor: "rgb(234, 236, 244)",
                                        drawBorder: false,
                                        borderDash: [2],
                                        zeroLineBorderDash: [2]
                                    }
                                }],
                                xAxes: [{
                                    offset: true
                                }]

                            },

                            tooltips: {
                                callbacks: {
                                    label: function(tooltipItem) {
                                        return tooltipItem.yLabel;
                                    }
                                },
                                titleMarginBottom: 10,
                                titleFontColor: '#6e707e',
                                titleFontSize: 14,
                                backgroundColor: "rgb(255,255,255)",
                                bodyFontColor: "#858796",
                                borderColor: '#dddfeb',
                                borderWidth: 1,
                                xPadding: 15,
                                yPadding: 15,
                                displayColors: false,
                                caretPadding: 10,
                            },
                            layout: {
                                padding: {
                                    top: 25,
                                    bottom: 20
                                }
                            }
                        };


                        clearChart();
                        var graphTarget = $("#multiBarChart");
                        var barGraph = new Chart(graphTarget, {
                            type: 'bar',
                            data: chartdata,
                            options: chartOptions
                        });
                    } else {
                        clearChart();
                        clearHtml();
                    }
                });

            } else {
                clearChart();
                clearHtml();
            }
        }

        function getData(cpuTypes) {
            var app = $('#appDrop')[0].value;
            var runTypes = [];
            var cpuList = cpuTypes;

            if (flag === 1) {
                $("#typeCheckBox input:checked").each(function() {
                    runTypes.push($(this).val());
                });
            } else {
                runTypes.push(typeVal);
            }

            var params = {};
            params.cpuList = cpuList;

            if ((runTypes.length > 1 && cpuList.length >= 1) || (runTypes.length >= 1 && cpuList.length > 1)) {
                var params = {};
                params.cpuList = cpuList;
                params.runTypes = runTypes;

                $.getJSON("/chart/multiCPUTable/" + app, $.param(params, true), function(data) {

                    if (data.scalingResultData.length > 1) {
                        updateTable(data.nodeLabel, data.scalingResultData);


                        updateTableRaw(data.nodeLabel.slice(0, -1), data.rawResult);
                        $("#p1").show();
                        $("#p2").show();
                        $('#footnote').show();
                    }
                });
            } else {
                clearHtml();
            }
        }

        function updateTable(columns, data) {

            var table;
            if (Object.keys(data).length > 0) {
                table = "<table class='table table-responsive table-bordered '>" + getHeaders(columns) + getBody(columns, data) + "</table>";
            }

            $('#tableNew').html(table);
        }

        function updateTableRaw(columns, data) {

            var table;
            if (Object.keys(data).length > 0) {
                table = "<table class='table table-responsive table-bordered '>" + getHeaders(columns) + getBody(columns, data) + "</table>";
            }

            $('#tableRaw').html(table);
        }

        function getHeaders(columns) {
            var headers = ['<thead><tr>'];

            columns.forEach(function(column) {
                headers.push('<th bgcolor="#D3D3D3">' + column + '</th>')
            });
            headers.push('</tr></thead>');

            return headers.join('');
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
            var row = ['<tr>'];
            var val;

            columns.forEach(function(column) {
                if (rowData[column] != undefined) {
                    val = rowData[column];
                } else {
                    val = '';
                }

                if (column === "Average") {
                    row.push('<td bgcolor="#C8E6C9">' + val + '</td>')
                } else if (column != "" && column != "Average") {
                    row.push("<td>" + val + "</td>")
                } else {
                    row.push('<td bgcolor="#D3D3D3" style="font-weight:bold">' + val + '</td>')
                }
            });

            row.push('</tr>');

            return row.join('');
        }

    } else {
        clearChart();
        clearHtml();
    }

}

function clearHtml() {
    var cpuList = [];
    $('#tableNew').html('');
    $('#tableRaw').html('');
    $('#footnote').hide();
    $("#p1").hide();
    $("#p2").hide();
    $('.collapse').collapse('hide');
}


function clearChart() {
    var cpuList = [];
    $('#multiBarChart').remove();
    $('#multiChart').append('<canvas id="multiBarChart" width="450" height="300" role="img"></canvas>');
}