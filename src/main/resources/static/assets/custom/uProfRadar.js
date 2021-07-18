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

                        },

                        tooltips: {
                              callbacks: {

                                title: function(tooltipItem, chartdata) {
                                    return chartdata.labels[tooltipItem[0].index];
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

}
