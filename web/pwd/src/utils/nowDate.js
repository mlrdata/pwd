export default {
	getymd() {
		const nowDate = new Date();
		const month = nowDate.getMonth() < 9 ? "0" + (nowDate.getMonth()+1) : nowDate.getMonth()+1;
		const date = nowDate.getDate() < 9 ? "0" + nowDate.getDate() : nowDate.getDate();
		return nowDate.getFullYear()+"-"+month + "-" +date;
	},
	
	getym() {
		const nowDate = new Date();
		const month = nowDate.getMonth() < 9 ? "0" + (nowDate.getMonth()+1) : nowDate.getMonth()+1;
		return nowDate.getFullYear()+"-"+month;
	}
}
