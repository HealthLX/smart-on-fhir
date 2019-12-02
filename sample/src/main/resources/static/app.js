		Vue.use(VueRouter);
		ELEMENT.locale(ELEMENT.lang.en);

		function qs(query = {}) {
			return Object.entries(query).map(q => q[1] ? q.join("=") : null).filter(Boolean).join("&");
		}
		function errorMessage(data) {
			return data ? 'Error: ' + data : 'Error happened.';
		}

    function successMessage(response) {
         return response.status === 200 ? 'Message sent.'  : response.data;
    }


		var hlxPatientForm = Vue.component("hlx-patient-form", {
			template: "#hlx-patient-form",
			data() {
				return {
					loading: false,
					context: {
						patient: {}
					}
				};
			},
			mounted() {
				this.loading = true;
				axios.get("/get-patient").then(response => {
					Object.assign(this.context.patient, response.data);
				}).catch(error => {
					this.$message.error(errorMessage(error.response.data.message));
				}).finally(() => {
					this.loading = false;
				});
			}
		});

		var router = new VueRouter({
			mode: "history",
			routes: [{
				path: "*",
				component: hlxPatientForm
			}]
		});

		var app = new Vue({
			el: "#app",
			router
		});