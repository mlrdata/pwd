export default {
	getUser() {
		let session = sessionStorage.getItem("erpUser");
		if (session) {
			 session = JSON.parse(session)
			 return session
		} else {
			return {
				account: ""
			}
		}
	},
	removeUser(){
		sessionStorage.removeItem("erpUser");
	}
	
}
