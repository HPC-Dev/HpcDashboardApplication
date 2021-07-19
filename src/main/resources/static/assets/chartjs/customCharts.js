Chart.defaults.global.defaultFontStyle = 'bold';
Chart.defaults.global.defaultFontFamily = 'Verdana';

var BACKGROUND_COLORS = ['#FF9E80', '#03A9F4', '#FFD180', '#9575CD', '#90A4AE', '#F9A825', '#00897B', '#C5E1A5', '#80CBC4', '#7986CB', '#7E57C2', '#3949AB', '#e57373', '#546E7A', '#A1887F'];

var BACKGROUND_COLORS_NEW = ['rgb(19,91,105)', 'rgb(21,104,121)', 'rgb(20,116,132)', 'rgb(133,155,163)', 'rgb(173,183,191)', 'rgba(255, 99, 132, 0.2)', 'rgba(75, 192, 192, 0.2)', 'rgba(255, 206, 86, 0.2)', 'rgba(153, 102, 255, 0.2)', 'rgba(255, 159, 64, 0.2)', 'rgba(54, 162, 235, 0.2)', 'rgba(192, 0, 0, 0.2)', '#FF9E80', '#03A9F4', '#FFD180', , '#90A4AE', '#F9A825', '#C5E1A5', '#80CBC4', '#7986CB', '#7E57C2', '#3949AB', '#e57373', '#546E7A', '#A1887F'];

$('#cpuDrop').change(cpuChange);
$('#typeDrop').change(typeChange);
$('#appDrop').change(appChange);

function cpuChange() {

    var preType = $("#typeDrop option:selected").val();
    var preApp = $("#appDrop option:selected").val();

    cpu = $('#cpuDrop')[0].value;
    if (cpu) {
        $("#type").show();

        $.getJSON("/runTypesByCPU", {
            cpu: cpu,
            ajax: 'true'
        }, function(data) {
            var html = '<option value="" selected="true" disabled="disabled">-- Run Type --</option>';
            var len = data.length;
            for (var i = 0; i < len; i++) {
                html += '<option value="' + data[i] + '">' +
                    data[i] + '</option>';
            }
            html += '</option>';
            $('#typeDrop').html(html);

            if (data.includes(preType)) {
                $('#typeDrop').val(preType);
                typeChange(preApp);
            }
             else if (data.includes('latest')) {
                                     $('#typeDrop').val('latest');
                                     typeChange(preApp);
                                      }
            else {
                $('#typeDrop').val('');
            }
        });
    }

    clearForm();
    clearChart();
}


function typeChange(preApp) {
    cpu = $('#cpuDrop')[0].value;
    type = $('#typeDrop')[0].value;


    if($("#appDrop option:selected").val())
    {
        var preApp = $("#appDrop option:selected").val();
    }

    if (cpu && type) {
        $("#app").show();
        $.getJSON("/appsByType", {
            cpu: cpu,
            type: type,
            ajax: 'true'
        }, function(data) {
            var html = '<option value="" selected="true" disabled="disabled">-- App --</option>';
            var len = data.length;
            for (var i = 0; i < len; i++) {
                html += '<option value="' + data[i] + '">' +
                    data[i] + '</option>';
            }
            html += '</option>';
            $('#appDrop').html(html);

            if (data.includes(preApp)) {
                $('#appDrop').val(preApp);
                appChange();
            } else {
                $('#appDrop').val('');
            }

        });
    }

    clearForm();
    clearChart();

    if ($("#option1").is(":checked")) {
        getNodeChartData();
    }
    if ($("#option2").is(":checked")) {
        getBmChartData();
    }
}


function clearForm() {
    $('#comment').empty();
    $('#appDrop').val('');
    $("#radio").hide();
    $("#option1").prop("checked", false);
    $("#option2").prop("checked", false);
    $("#noChart").hide();
    $('#tableScaling').html('');
    $('#tableNode').html('');
}

function clearChart() {
    $('#bmBarChart').remove();
    $('#bmChart').append('<canvas id="bmBarChart" width="450" height="300" role="img"></canvas>');

    $('#nodeBarChart').remove();
    $('#nodeChart').append('<canvas id="nodeBarChart" width="450" height="300" role="img"></canvas>');

    $('#ctx').remove();
    $('#multiChart').append('<canvas id="ctx" width="450" height="300" role="img"></canvas>');


}

function appChange() {
    clearChart();
    var app = $('#appDrop')[0].value;
    var type = $('#typeDrop')[0].value;
    var cpu = $('#cpuDrop')[0].value;

    if (app && type && cpu) {
        $.getJSON("/chart/getNodesCount", {
            appName: app,
            cpu: cpu,
            type: type,
            ajax: 'true'
        }, function(data) {

            if (data > 1) {
                $("#radio").show();
                if ($("#option1").is(":checked")) {
                    $('#tableScaling').html('');
                    getNodeChartData();
                }

                if ($("#option2").is(":checked")) {
                    getBmChartData();
                }
            } else {
                $("#radio").hide();
                $("#multiChart").hide();
                $("#nodeChart").show();
                $("#noChart").hide();
                $('#comment').empty();
                $('#tableScaling').html('');
                getNodeChartData();
            }
        });
    }

    $("#noChart").hide();
    $('#comment').empty();
    $('#tableScaling').html('');
    $('#tableNode').html('');

}

$("#option1")
    .change(function() {
        if ($(this).is(":checked")) {
            var val = $(this).val();
            if (val === "node") {
                $("#multiChart").hide();
                $("#nodeChart").show();
                $("#noChart").hide();
                $('#comment').empty();
                $('#tableScaling').html('');
                getNodeChartData();
            }
        }
    });


$("#option2")
    .change(function() {
        var app = $('#appDrop')[0].value;
        var cpu = $('#cpuDrop')[0].value;
        if ($(this).is(":checked")) {
            var val = $(this).val();
            if (val === "bench") {
                $("#nodeChart").hide();
                $('#comment').empty();
                $("#multiChart").show();
                $('#tableNode').html('');
                getBmChartData();
            }
        }
    });


function getNodeChartData() {
    var cpu = $('#cpuDrop')[0].value;
    var app = $('#appDrop')[0].value;
    var type = $('#typeDrop')[0].value;

    var node = 1;
    if (app && cpu && node && type) {
        $.getJSON("/chart/resultApp/" + cpu + "/" + app + "/" + node + "/" + type, function(data) {
            var label = data[0].labels;
            var result = data[0].dataset;
            if (result.length > 3) {
                var chartdata = {
                    labels: label,
                    datasets: [{
                        backgroundColor: ['#FF9E80', '#03A9F4', '#FFD180', '#9575CD', '#90A4AE', '#F9A825', '#00897B', '#C5E1A5', '#80CBC4', '#7986CB', '#7E57C2', '#3949AB', '#e57373', '#546E7A', '#A1887F'],
                        borderWidth: 1,
                        data: result
                    }]
                };
            } else {
                var chartdata = {
                    labels: label,
                    datasets: [{
                        backgroundColor: ['#FF9E80', '#03A9F4', '#FFD180', '#9575CD', '#90A4AE', '#F9A825', '#00897B', '#C5E1A5', '#80CBC4', '#7986CB', '#7E57C2', '#3949AB', '#e57373', '#546E7A', '#A1887F'],
                        borderWidth: 1,
                        data: result,
                        barPercentage: 0.2
                    }]
                };
            }
            var chartOptions = {

                legend: {
                    display: false,
                },
                title: {
                    display: true,
                    text: data[0].appCPUName
                },
                scales: {
                    yAxes: [{
                        ticks: {
                            beginAtZero: true
                        },
                        scaleLabel: {
                            display: true,
                            labelString: data[0].metric,
                            fontStyle: "bold"
                        },
                        gridLines: {
                            color: "rgb(234, 236, 244)",
                            zeroLineColor: "rgb(234, 236, 244)",
                            drawBorder: false,
                            borderDash: [2],
                            zeroLineBorderDash: [2]
                        }
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
            var graphTarget = $("#nodeBarChart");
            var barGraph = new Chart(graphTarget, {
                type: 'bar',
                data: chartdata,
                options: chartOptions
            });

            $('#comment').empty();
            var comment = " <p style='font-weight: bold;font-size:12px;text-align:left;font-family:verdana;'>" + "*" + data[0].comment + "</p>"
            $('#comment').append(comment);
            $('#comment').show();
            updateTableNode(data[0].labelsTable, data[0].tableDataset);
        });
    } else {
        $('#tableNode').html('');
        $('#comment').empty();
        clearChart();
    }
}

function getRandomColorHex() {
    var hex = "0123456789ABCDEF",
        color = "#";
    for (var i = 1; i <= 6; i++) {
        color += hex[Math.floor(Math.random() * 16)];
    }
    return color;
}

function getData() {
    var cpu = $('#cpuDrop')[0].value;
    var app = $('#appDrop')[0].value;
    var type = $('#typeDrop')[0].value;

    if (app && cpu && type) {

        $.getJSON("/chart/scalingTable/" + cpu + "/" + app + "/" + type, function(data) {
            if (data.scalingResultData.length > 1) {
                updateTable(data.nodeLabel, data.scalingResultData);
            }
        });
    } else {
        $('#tableScaling').html('');
    }
}

function updateTableNode(columns, data) {

    var table;
    if (Object.keys(data).length > 0) {
        table = "<table class='table table-responsive table-bordered '>" + getHeaders(columns) + getBodySingle(columns, data) + "</table>";
    }

    $('#tableNode').html(table);
}

function updateTable(columns, data) {

    var table;
    if (Object.keys(data).length > 0) {
        table = "<table class='table table-responsive table-bordered '>" + getHeaders(columns) + getBody(columns, data) + "</table>";
    }

    $('#tableScaling').html(table);
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
        row.push("<td>" + val + "</td>")
    });

    row.push('</tr>');

    return row.join('');
}

function getBodySingle(columns, data) {
    var body = ['<tbody>'];
    data.forEach(function(row) {
        body.push(generateRowSingle(columns, row))
    });
    body.push('</tbody>');

    return body.join('');
}

function generateRowSingle(columns, rowData) {
    var row = ['<tr>'];
    var val;

    columns.forEach(function(column) {
        if (rowData[column] != undefined) {
            val = rowData[column];
        } else {
            val = '';
        }
        if (column != "") {
            row.push("<td>" + val + "</td>")
        } else {
            row.push('<td bgcolor="#D3D3D3" style="font-weight:bold">' + val + '</td>')
        }

    });

    row.push('</tr>');

    return row.join('');
}


function getBmChartData() {

    var cpu = $('#cpuDrop')[0].value;
    var app = $('#appDrop')[0].value;
    var type = $('#typeDrop')[0].value;

    getData();
    if (app && cpu && type) {
        $.getJSON("/chart/resultBm/" + cpu + "/" + app + "/" + type, function(data) {
            var dataPoints = [];
            var point = [];
            $.each(data.dataset, function(key, val) {
                point = [];
                $.each(val, function(key, value) {
                    point.push({
                        x: key,
                        y: value
                    });

                });
                dataPoints.push(point);

            });

            var label = data.labels;
            var result = dataPoints;

            if (result.length > 0) {
                $("#nodeChart").hide();
                $("#multiChart").show();
                var chart = new Chart(ctx, {

                    type: 'scatter',
                    data: {
                        datasets: result.map(function(dataset, index) {
                            return {
                                label: label[index],
                                data: result[index],
                                borderWidth: 1,
                                pointBackgroundColor: [BACKGROUND_COLORS_NEW[index], BACKGROUND_COLORS_NEW[index], BACKGROUND_COLORS_NEW[index], BACKGROUND_COLORS_NEW[index], BACKGROUND_COLORS_NEW[index]],
                                borderColor: BACKGROUND_COLORS_NEW[index],
                                pointRadius: 5,
                                pointHoverRadius: 5,
                                fill: false,
                                tension: 0,
                                showLine: true
                            };
                        })
                    },
                    options: {
                        responsive: true,
                        maintainAspectRatio: false,

                        legend: {
                            display: true,
                            position: 'bottom',
                        },
                        title: {
                            display: true,
                            text: data.appCPUName,
                            fontStyle: "bold"
                        },
                        scales: {
                            xAxes: [{
                                ticks: {
                                    min: 0,
                                    max: 20,
                                    stepSize: 5
                                },
                                gridLines: {
                                    drawOnChartArea: false
                                },
                                scaleLabel: {
                                    display: true,
                                    labelString: 'Nodes',
                                    fontStyle: "bold"
                                }
                            }],
                            yAxes: [{
                                ticks: {
                                    min: 0,
                                    max: 25,
                                    stepSize: 5,
                                    padding: 10
                                },
                                gridLines: {
                                    drawOnChartArea: true,
                                    borderDash: [2],
                                    zeroLineBorderDash: [2]
                                },
                                scaleLabel: {
                                    display: true,
                                    labelString: 'Node Scaling',
                                    fontStyle: "bold"
                                }
                            }]

                        }
                    }
                });
            } else {
                $("#noChart").show();
                $('#tableScaling').html('');
                $('#tableNode').html('');
            }
        });
    }
}