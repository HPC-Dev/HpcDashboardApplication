var BACKGROUND_COLORS = ['rgb(19,91,105)', 'rgb(255, 159, 64)', 'rgb(75, 192, 192)'];

Chart.defaults.global.defaultFontStyle = 'bold';
Chart.defaults.global.defaultFontFamily = 'Verdana';

var app;
var cpuList = [];
var typeList = [];

function clearHtml() {
    $('#heading').empty();
    $('#footnote').hide();
    $('.collapse').collapse('hide')
}


$('#cpuDrop1').on("change", function() {
    var value = $(this).val();

    var preValue1 = $("#typeDrop1 option:selected").val();
    // $("#type1").show();
    clearHtml();
    $('#tableHeatMap').html('');
    if (value != '') {
        $.getJSON("/runTypesByCPUUProf", {

            cpu: $(this).val(),
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
              }
              else{
              $('#typeDrop1').val('');
              }


            setTimeout(getData, 40);
        });
    }

});


$('#cpuDrop2').on("change", function() {
    var value = $(this).val();
    var preValue2 = $("#typeDrop2 option:selected").val();

    clearHtml();
    // $("#type2").show();
    $('#tableHeatMap').html('');
    if (value != '') {
        $.getJSON("/runTypesByCPUUProf", {
            cpu: $(this).val(),
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
              }
              else{
              $('#typeDrop2').val('');
             }


            setTimeout(getData, 40);

        });

    }


});


$('#cpuDrop3').on("change", function() {
    var value = $(this).val();
    var preValue3 = $("#typeDrop3 option:selected").val();
    clearHtml();
    // $("#type2").show();
    $('#tableHeatMap').html('');
    if (value != '') {
        $.getJSON("/runTypesByCPUUProf", {
            cpu: $(this).val(),
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
             }
             else{
             $('#typeDrop3').val('');
                          }

           setTimeout(getData, 40);
        });

    }


});


$('#cpuDrop4').on("change", function() {
    var value = $(this).val();
    var preValue4 = $("#typeDrop4 option:selected").val();
    clearHtml();
    // $("#type2").show();
    $('#tableHeatMap').html('');
    if (value != '') {
        $.getJSON("/runTypesByCPUUProf", {
            cpu: $(this).val(),
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
            }
            else{
             $('#typeDrop4').val('');
            }

            setTimeout(getData, 40);
        });

    }


});


$('#type1').on("change", function() {
setTimeout(getData, 40);
});

$('#type2').on("change", function() {
setTimeout(getData, 40);
});

$('#type3').on("change", function() {
setTimeout(getData, 40);

});

$('#type4').on("change", function() {
setTimeout(getData, 40);

});



function captureCPUsTypes(){
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
        if (cpuList[i].includes("CPU") || cpuList[i] == "" ) {
            cpuList.splice(i, 1);
            i--;
        }
    }

}

function getData() {
    clearHtml();
    $('#tableHeatMap').html('');

    captureCPUsTypes();

    if ((cpuList.length > 1 && typeList.length > 1 && (cpuList.length == typeList.length)) && !(cpu1 === cpu2 && type1 === type2)) {

        var params = {};
        params.cpuList = cpuList;
        params.typeList = typeList;

        $.getJSON("/uProfRadarChart/", $.param(params, true), function(data) {

                    if (data.dataset.length > 1) {
                           var chartdata = {
                                labels: data.metrics,
                                datasets: data.dataset.map(function(dataset, index) {
                                    return {
                                        label: dataset.procAppBM,
                                        borderColor: BACKGROUND_COLORS[index],
                                        data: dataset.value,
                                        };
                                })
                            };


                        var chartOptions = {

                       legend: {
                            display: true,
                            position: 'right'
                           },
//                        title: {
//                            display: true,
//                            text: 'uProf Radar Chart'
//                        },
                        scale: {
                           yAxes: [{
                                     ticks: {
                                     beginAtZero: true,
                                     },
                                   }],
                           xAxes: [{
                                  offset: true
                                  }]
                        },

                        tooltips: {
                              callbacks: {

                              title: function(tooltipItem) {
                                  return  tooltipItem.xLabel;
                                },

                                label: function(tooltipItem, data) {
                                  return data.datasets[tooltipItem.datasetIndex].label + ":  " + tooltipItem.yLabel;
                                }
                              }
                            }

                        };

                        clearChart();
                        var graphTarget = $("#uProfRadarChart");
                        var radarGraph = new Chart(graphTarget, {
                            type: 'radar',
                            data: chartdata,
                            options: chartOptions
                        });


                    } else {
                        clearChart();
                     }


        });
    } else {
        clearChart();

    }



function clearChart() {
    var cpuList = [];
    $('#uProfRadarChart').remove();
    $('#uProfRadar').append('<canvas id="uProfRadarChart" width="450" height="300" role="img"></canvas>');
}

//    function generateChart(data)
//    {
//
//    window.chartColors = {
//      red: 'rgb(255, 99, 132)',
//      orange: 'rgb(255, 159, 64)',
//      yellow: 'rgb(255, 205, 86)',
//      green: 'rgb(75, 192, 192)',
//      blue: 'rgb(54, 162, 235)',
//      purple: 'rgb(153, 102, 255)',
//      grey: 'rgb(231,233,237)'
//    };
//
//    window.onload = function() {
//      window.myRadar = new Chart(document.getElementById("canvas"), config);
//    };
//    var colorNames = Object.keys(window.chartColors);
//
//
//var label1 ="cfx_50";
//var label2 ="aw14";
//var label3 ="3cars";
//
//var color = Chart.helpers.color;
//var config = {
//  type: 'radar',
//  data: {
//    labels: [
//      "CPU Utilization (%)", "CPU Eff Freq", "IPC (Sys + User)","Retired SSE/AVX Flops(GFLOPs)", "Tot Mem", "Tot Mem Read","Tot Mem Write", "Tot xGMI BW", "L3 Hit %"],
//    datasets: [{
//      label: label1,
//      backgroundColor: color(window.chartColors.red).alpha(0.2).rgbString(),
//      borderColor: window.chartColors.red,
//      pointBackgroundColor: window.chartColors.red,
//      data: [80,73,67,25,52,35,16,30,41],
//    }, {
//      label: label2,
//      backgroundColor: color(window.chartColors.blue).alpha(0.2).rgbString(),
//      borderColor: window.chartColors.blue,
//      pointBackgroundColor: window.chartColors.blue,
//      data: [92,80,84,19,43,36,7,35,41],
//    },{
//      label: label3,
//      backgroundColor: color(window.chartColors.purple).alpha(0.2).rgbString(),
//      borderColor: window.chartColors.purple,
//      pointBackgroundColor: window.chartColors.purple,
//      data: [99,83,85,82,28,19,9,42,80],
//    } ]
//  },
//  options: {
//    legend: {
//      position: 'top',
//    },
//    title: {
//      display: true,
//      text: 'Chart.js Outcome Graph'
//    },
//    scale: {
//      ticks: {
//        beginAtZero: true
//      }
//    }
//  }
//};
//}




}
