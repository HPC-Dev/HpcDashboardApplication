var BACKGROUND_COLORS = ['rgb(19,91,105)', 'rgb(255, 159, 64)', 'rgb(75, 192, 192)', 'rgb(178, 102, 255)'];
Chart.defaults.global.defaultFontStyle = 'bold';
Chart.defaults.global.defaultFontFamily = 'Verdana';
var app;
var cpuList = [];
var typeList = [];
var cpuFlag = [];
var sliderOne = [];
var sliderTwo = [];
var sliderThree = [];
var sliderFour = [];
var max1 = 100;
var max2 = 100;
var max3 = 100;
var max4 = 100;
var from1;
var to1;
var from2;
var to2;
var from3;
var to3;
var from4;
var to4;
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

function clearHtml() {
	$('#heading').empty();
	$('#footnote').hide();
	$('.collapse').collapse('hide')
}
$('#cpuDrop1').on("change", function() {
	$('#slider1').html('');
	$('#slider1').html('<input type="text" id="slider_id1" name="slider1" value="" />');
	from1 = 0;
	to1 = 0;
	var value = $(this).val();
	var preValue1 = $("#typeDrop1 option:selected").val();
	// $("#type1").show();
	clearHtml();
	$('#tableHeatMap').html('');
	if(value != '') {
		$.getJSON("/runTypesByCPUUProf", {
			cpu: $(this).val(),
			ajax: 'true'
		}, function(data) {
			var html = '<option value="" selected="true" disabled="disabled">-- RunType1 --</option>';
			var len = data.length;
			for(var i = 0; i < len; i++) {
				html += '<option value="' + data[i] + '">' + data[i] + '</option>';
			}
			html += '</option>';
			$('#typeDrop1').html(html);
			if(data.includes(preValue1)) {
				$('#typeDrop1').val(preValue1);
			} else if(data.includes('latest')) {
				$('#typeDrop1').val('latest');
			} else {
				$('#typeDrop1').val('');
			}
			setTimeout(getData1, 40);
		});
	}
});
$('#cpuDrop2').on("change", function() {
	$('#slider2').html('');
	$('#slider2').html('<input type="text" id="slider_id2" name="slider2" value="" />');
	from2 = 0;
	to2 = 0;
	var value = $(this).val();
	var preValue2 = $("#typeDrop2 option:selected").val();
	clearHtml();
	$('#tableHeatMap').html('');
	if(value != '') {
		$.getJSON("/runTypesByCPUUProf", {
			cpu: $(this).val(),
			ajax: 'true'
		}, function(data) {
			var html = '<option value="" selected="true" disabled="disabled">-- RunType2 --</option>';
			var len = data.length;
			for(var i = 0; i < len; i++) {
				html += '<option value="' + data[i] + '">' + data[i] + '</option>';
			}
			html += '</option>';
			$('#typeDrop2').html(html);
			if(data.includes(preValue2)) {
				$('#typeDrop2').val(preValue2);
			} else if(data.includes('latest')) {
				$('#typeDrop2').val('latest');
			} else {
				$('#typeDrop2').val('');
			}
			setTimeout(getData1, 40);
		});
	}
});
$('#cpuDrop3').on("change", function() {
	$('#slider3').html('');
	$('#slider3').html('<input type="text" id="slider_id3" name="slider3" value="" />');
	from3 = 0;
	to3 = 0;
	var value = $(this).val();
	var preValue3 = $("#typeDrop3 option:selected").val();
	clearHtml();
	// $("#type2").show();
	$('#tableHeatMap').html('');
	if(value != '') {
		$.getJSON("/runTypesByCPUUProf", {
			cpu: $(this).val(),
			ajax: 'true'
		}, function(data) {
			var html = '<option value="" selected="true" disabled="disabled">-- RunType3 --</option>';
			var len = data.length;
			for(var i = 0; i < len; i++) {
				html += '<option value="' + data[i] + '">' + data[i] + '</option>';
			}
			html += '</option>';
			$('#typeDrop3').html(html);
			if(data.includes(preValue3)) {
				$('#typeDrop3').val(preValue3);
			} else if(data.includes('latest')) {
				$('#typeDrop3').val('latest');
			} else {
				$('#typeDrop3').val('');
			}
			setTimeout(getData1, 40);
		});
	}
});
$('#cpuDrop4').on("change", function() {
	$('#slider4').html('');
	$('#slider4').html('<input type="text" id="slider_id4" name="slider4" value="" />');
	from4 = 0;
	to4 = 0;
	var value = $(this).val();
	var preValue4 = $("#typeDrop4 option:selected").val();
	clearHtml();
	// $("#type2").show();
	$('#tableHeatMap').html('');
	if(value != '') {
		$.getJSON("/runTypesByCPUUProf", {
			cpu: $(this).val(),
			ajax: 'true'
		}, function(data) {
			var html = '<option value="" selected="true" disabled="disabled">-- RunType4 --</option>';
			var len = data.length;
			for(var i = 0; i < len; i++) {
				html += '<option value="' + data[i] + '">' + data[i] + '</option>';
			}
			html += '</option>';
			$('#typeDrop4').html(html);
			if(data.includes(preValue4)) {
				$('#typeDrop4').val(preValue4);
			} else if(data.includes('latest')) {
				$('#typeDrop4').val('latest');
			} else {
				$('#typeDrop4').val('');
			}
			setTimeout(getData1, 40);
		});
	}
});
$('#type1').on("change", function() {
	setTimeout(getData1, 40);
});
$('#type2').on("change", function() {
	setTimeout(getData1, 40);
});
$('#type3').on("change", function() {
	setTimeout(getData1, 40);
});
$('#type4').on("change", function() {
	setTimeout(getData1, 40);
});

function sliderGenerate() {
	if($('#cpuDrop1')[0].value && $('#typeDrop1')[0].value) {
		$("#slider_id1").ionRangeSlider({
			type: "double",
			min: 0,
			max: max1,
			from: 15,
			to: max1 - 15,
			onFinish: function(data) {
				from1 = data.from;
				to1 = data.to;
				sliderOne.length = 0;
				getData1();
			}
		});
	}
	if($('#cpuDrop2')[0].value && $('#typeDrop2')[0].value) {
		$("#slider_id2").ionRangeSlider({
			type: "double",
			min: 0,
			max: max2,
			from: 15,
			to: max2 - 15,
			onFinish: function(data) {
				from2 = data.from;
				to2 = data.to;
				sliderTwo.length = 0;
				getData1();
			}
		});
	}
	if($('#cpuDrop3')[0].value && $('#typeDrop3')[0].value) {
		$("#slider_id3").ionRangeSlider({
			type: "double",
			min: 0,
			max: max3,
			from: 15,
			to: max3 - 15,
			onFinish: function(data) {
				from3 = data.from;
				to3 = data.to;
				sliderThree.length = 0;
				getData1();
			}
		});
	}
	if($('#cpuDrop4')[0].value && $('#typeDrop4')[0].value) {
		$("#slider_id4").ionRangeSlider({
			type: "double",
			min: 0,
			max: max4,
			from: 15,
			to: max4 - 15,
			onFinish: function(data) {
				from4 = data.from;
				to4 = data.to;
				sliderFour.length = 0;
				getData1();
			}
		});
	}
}

function captureCPUsTypes() {
	cpuList = [];
	typeList = [];
	cpuFlag = [];
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
	if(cpu1) {
		cpuFlag.push("One");
	}
	if(cpu2) {
		cpuFlag.push("Two");
	}
	if(cpu3) {
		cpuFlag.push("Three");
	}
	if(cpu4) {
		cpuFlag.push("Four");
	}
	typeList.push(type1);
	typeList.push(type2);
	typeList.push(type3);
	typeList.push(type4);
	var filteredTypeList = typeList.filter(function(type) {
		return type != "";
	});
	typeList = filteredTypeList;
	for(var i = 0; i < cpuList.length; i++) {
		if(cpuList[i].includes("CPU") || cpuList[i] == "") {
			cpuList.splice(i, 1);
			i--;
		}
	}
}

function getSliderValues(params) {
	params.cpuFlag = cpuFlag;
	var promise = $.getJSON("/uProfSliders/", $.param(params, true));
	promise.done(function(data) {
		for(const [key, value] of Object.entries(data)) {
			if(key == "One") max1 = value;
			else if(key == "Two") max2 = value;
			else if(key == "Three") max3 = value;
			else if(key == "Four") max4 = value;
		}
		sliderGenerate();
		fillSliderValues(params);
	});
}

function fillSliderValues(params) {
	if(from1 || to1) {
		sliderOne.length = 0;
		sliderOne.push(from1);
		sliderOne.push(to1);
	} else {
		sliderOne.length = 0;
		sliderOne.push(15);
		sliderOne.push(max1 - 15);
	}
	if(from2 || to2) {
		sliderTwo.length = 0;
		sliderTwo.push(from2);
		sliderTwo.push(to2);
	} else {
		sliderTwo.length = 0;
		sliderTwo.push(15);
		sliderTwo.push(max2 - 15);
	}
	if(from3 || to3) {
		sliderThree.length = 0;
		sliderThree.push(from3);
		sliderThree.push(to3);
	} else {
		sliderThree.length = 0;
		sliderThree.push(15);
		sliderThree.push(max3 - 15);
	}
	if(from4 || to4) {
		sliderFour.length = 0;
		sliderFour.push(from4);
		sliderFour.push(to4);
	} else {
		sliderFour.length = 0;
		sliderFour.push(15);
		sliderFour.push(max4 - 15);
	}
	addToStore("One", sliderOne);
	addToStore("Two", sliderTwo);
	addToStore("Three", sliderThree);
	addToStore("Four", sliderFour);
	params.sliderList = JSON.stringify(store);
	$.getJSON("/uProfRadarChartSlider/", $.param(params, true), function(data) {
		if(data.dataset.length >= 1) {
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
}

function getData1() {
	clearHtml();
	$('#tableHeatMap').html('');
	captureCPUsTypes();
	if((cpuList.length >= 1 && typeList.length >= 1 && (cpuList.length == typeList.length))) {
		var params = {};
		params.cpuList = cpuList;
		params.typeList = typeList;
		//setTimeout(fillSliderValues, 40);
		getSliderValues(params);
	} else {
		clearChart();
	}
}
//function getData() {
//    clearHtml();
//    $('#tableHeatMap').html('');
//
//    captureCPUsTypes();
//
//    if ((cpuList.length >= 1 && typeList.length >= 1 && (cpuList.length == typeList.length))) {
//
//        var params = {};
//        params.cpuList = cpuList;
//        params.typeList = typeList;
//
//        $.getJSON("/uProfSliders/", $.param(params, true), function(data) {
//
//            max1 = data[0];
//
//            if ($('#cpuDrop2')[0].value && $('#typeDrop2')[0].value)
//                max2 = data[1];
//
//            if ($('#cpuDrop3')[0].value && $('#typeDrop3')[0].value)
//                max3 = data[2];
//
//            if ($('#cpuDrop4')[0].value && $('#typeDrop4')[0].value)
//                max4 = data[3];
//
//            sliderGenerate();
//
//        });
//
//
//
//        $.getJSON("/uProfRadarChart/", $.param(params, true), function(data) {
//
//
//            if (data.dataset.length >= 1) {
//                var chartdata = {
//                    labels: data.metrics,
//                    datasets: data.dataset.map(function(dataset, index) {
//                        return {
//                            label: dataset.procAppBM,
//                            borderColor: BACKGROUND_COLORS[index],
//                            data: dataset.value,
//                        };
//                    })
//                };
//
//                var chartOptions = {
//
//                    legend: {
//                        display: true,
//                        position: 'right'
//                    },
//                    scale: {
//                        yAxes: [{
//                            ticks: {
//                                beginAtZero: true,
//                            },
//                        }],
//
//                    },
//                    tooltips: {
//                        callbacks: {
//
//                            title: function(tooltipItem, chartdata) {
//                                return chartdata.labels[tooltipItem[0].index];
//                            },
//
//                            label: function(tooltipItem, data) {
//                                return data.datasets[tooltipItem.datasetIndex].label + ":  " + tooltipItem.yLabel;
//                            }
//                        }
//                    }
//
//                };
//
//                clearChart();
//                var graphTarget = $("#uProfRadarChart");
//
//                var radarGraph = new Chart(graphTarget, {
//                    type: 'radar',
//                    data: chartdata,
//                    options: chartOptions
//                });
//
//
//            } else {
//                clearChart();
//            }
//
//
//        });
//    } else {
//        clearChart();
//
//    }
//
//}
function clearChart() {
	var cpuList = [];
	$('#uProfRadarChart').remove();
	$('#uProfRadar').append('<canvas id="uProfRadarChart" width="450" height="300" role="img"></canvas>');
}