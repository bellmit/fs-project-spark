/**
 * ͳ�ƴ�����
 */
(function (parent) {
	if(typeof parent == "undefined") return;
	if(typeof parent.Analytics != "undefined") return;
	var Analytics = function () {};
	Analytics.trigger = function (page, eve, params) {
		console.log(this);
	};
	parent.Analytics = Analytics;
	// ����ģ��
	Analytics.trigger();
})(window);