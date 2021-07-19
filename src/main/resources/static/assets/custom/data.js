Chart.defaults.global.defaultFontStyle = 'bold';
Chart.defaults.global.defaultFontFamily = 'Verdana';

var BACKGROUND_COLORS = ['#FF9E80', '#03A9F4', '#FFD180', '#9575CD', '#90A4AE', '#F9A825', '#00897B', '#C5E1A5', '#80CBC4', '#7986CB', '#7E57C2', '#3949AB', '#e57373', '#546E7A', '#A1887F'];

var BACKGROUND_COLORS_NEW = ['rgb(19,91,105)', 'rgb(21,104,121)', 'rgb(20,116,132)', 'rgb(133,155,163)', 'rgb(173,183,191)', 'rgba(255, 99, 132, 0.2)', 'rgba(75, 192, 192, 0.2)', 'rgba(255, 206, 86, 0.2)', 'rgba(153, 102, 255, 0.2)', 'rgba(255, 159, 64, 0.2)', 'rgba(54, 162, 235, 0.2)', 'rgba(192, 0, 0, 0.2)', '#FF9E80', '#03A9F4', '#FFD180', , '#90A4AE', '#F9A825', '#C5E1A5', '#80CBC4', '#7986CB', '#7E57C2', '#3949AB', '#e57373', '#546E7A', '#A1887F'];


function clearChart() {
$("#multiChart").hide();
    $('#ctx').remove();
    $('#multiChart').append('<canvas id="ctx" width="450" height="300" role="img"></canvas>');

}


var cpus = []
var typeVal;
var flag;
$('#typeDrop').change(typeChange);
$("#app").on("change", getData);
$("#app").on("change", getScalingChart);

$('#cpuDrop').on("change", function() {
    cpu = $('#cpuDrop')[0].value;
    var preType = $("#typeDrop option:selected").val();
    var preApp = $("#appDrop option:selected").val();

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

            getData();
            getScalingChart();
        });

    } else if (value == '') {
        $("#type").hide();
        $("#app").hide();
    }

    clearChart();
     $("#p1").hide();
     $("#p2").hide();
     $("#p3").hide();
     $("#p4").hide();
    $('#typeDrop').val('');
    $('#appDrop').val('');
    $('#table').html('');
    $('#tableScaling').html('');
    $('#tableCV').html('');
    $('#tableCount').html('');

});

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
                      getData();
                      getScalingChart();
                        } else {
                            $('#appDrop').val('');
                        }
        });
    } else if (value == '') {
        $("#app").hide();
    }

    clearChart();
    $('#appDrop').val('');
    $('#table').html('');
    $('#tableScaling').html('');
    $('#tableCV').html('');
    $('#tableCount').html('');

    getData();
getScalingChart();

}


function findCpuCore(cpu) {
    return cpus.find(function(eachCpu) {
        return eachCpu.cpuSku === cpu
    })['cores'];
}


function getScalingChart(){
 var cpu = $('#cpuDrop')[0].value;
    var app = $('#appDrop')[0].value;
    var runType = $('#typeDrop')[0].value;

if (app && cpu && runType) {

$.getJSON("/chart/resultBm/" + cpu + "/" + app + "/" + runType, function(data) {
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

            console.log(cpu);

            if (result.length > 0) {
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
            clearChart();
            }
        });
        }
}






function getData() {
    var cpu = $('#cpuDrop')[0].value;
    var app = $('#appDrop')[0].value;
    var runType = $('#typeDrop')[0].value;

    var comment;
    if (app && cpu && runType) {

        $.getJSON("/avg/result/" + cpu + "/" + app + "/" + runType, function(data) {
            var transformedData = [];
            var columnNames = ['Nodes', 'Cores'];
            data.forEach((item, index) => {
                var row = transformedData.find(function(tr) {
                    return tr.Nodes === item.nodes;
                });
                var rowIndex = transformedData.findIndex(function(tr) {
                    return tr.Nodes === item.nodes;
                })

                if (!row) {
                    row = {
                        Nodes: item.nodes,
                        Cores: item.cores
                    };
                    transformedData.push(row);
                }

                row[item.bmName] = item.avgResult;

                if (columnNames.indexOf(item.bmName) === -1) {
                    columnNames.push(item.bmName);
                }

            });
            updateTable(columnNames, transformedData);

        });

        $.getJSON("/chart/scalingTable/" + cpu + "/" + app + "/" + runType, function(data) {
                    comment = data.comment;
                    if(data.scalingResultData.length >1) {
                    updateTableScaling(data.nodeLabel, data.scalingResultData);
                    }
                    updateTableCV(data.nodeLabel, data.cvdata);
                    updateTableCount(data.nodeLabel, data.countData);
                });

    }


     clearChart();
     $('#table').html('');
     $('#tableScaling').html('');
     $('#tableCV').html('');
     $('#tableCount').html('');
     $("#p1").hide();
     $("#p2").hide();
     $("#p3").hide();
     $("#p4").hide();

}

function updateTable(columns, data) {
    var table;

    if (data.length > 0) {
        table = "<table class='table table-responsive table-bordered '>" + getHeaders(columns) + getBody(columns, data) + "</table>";
        //  table += " <p style='font-size:12px;text-align:left;font-family:verdana;'>" +"*" + comment + "</p>"
        $("#p1").show();

    } else {
        table = "<p>No data available</p>";
    }

    $('#table').html(table);
}

function updateTableScaling(columns, data) {
    var table;

    if (data.length > 0) {
        table = "<table class='table table-responsive table-bordered '>" + getHeaders(columns) + getBody(columns, data) + "</table>";
        $("#p2").show();
    }
    $('#tableScaling').html(table);
}

function updateTableCV(columns, data) {
    var table;

    if (data.length > 0) {
        table = "<table class='table table-responsive table-bordered '>" + getHeaders(columns) + getBodyVariance(columns, data) + "</table>";
        $("#p3").show();
    }
    $('#tableCV').html(table);
}

function updateTableCount(columns, data) {
    var table;

    if (data.length > 0) {
        table = "<table class='table table-responsive table-bordered '>" + getHeaders(columns) + getBodyCount(columns, data) + "</table>";
        $("#p4").show();
    }
    $('#tableCount').html(table);
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

        row.push('<td>' + val + '</td>')

    });

    row.push('</tr>');

    return row.join('');
}

function getBodyVariance(columns, data) {
    var body = ['<tbody>'];
    data.forEach(function(row) {
        body.push(generateRowVariance(columns, row))
    });

    body.push('</tbody>');

    return body.join('');
}


function generateRowVariance(columns, rowData) {
    var row = ['<tr>'];
    var val;

    columns.forEach(function(column) {

        if (rowData[column] != undefined) {
            val = rowData[column];

        } else {
            val = '';
        }

        if(column != 'Nodes' &&  column != 'Cores' && rowData[column] != undefined){

            if(val < 3.0){
                row.push('<td bgcolor="#C8E6C9">' + val.concat('%') +  '</td>')
            }
            else if(val > 3.0 && val < 5.0 ){
                row.push('<td bgcolor="#FFF9C4">' + val.concat('%') + '</td>')
            }
            else if(val > 5.0 ){
                row.push('<td bgcolor="FFCDD2">' + val.concat('%') + '</td>')
            }
        }
       else{
            row.push('<td>' + val + '</td>')
       }
    });

    row.push('</tr>');

    return row.join('');
}


function getBodyCount(columns, data) {
    var body = ['<tbody>'];
    data.forEach(function(row) {
        body.push(generateRowCount(columns, row))
    });

    body.push('</tbody>');

    return body.join('');
}


function generateRowCount(columns, rowData) {
    var row = ['<tr>'];
    var val;

    columns.forEach(function(column) {

        if (rowData[column] != undefined) {
            val = rowData[column];

        } else {
            val = '';
        }

        if(column != 'Nodes' &&  column != 'Cores' && rowData[column] != undefined){

            if(val < 3){
                row.push('<td bgcolor="#FFCDD2">' + val +  '</td>')
            }
            else {
                row.push('<td bgcolor="#C8E6C9">' + val + '</td>')
            }
        }
       else{
            row.push('<td>' + val + '</td>')
       }
    });

    row.push('</tr>');

    return row.join('');
}