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
					preview:{
					    text: '',
					    loaded: false,
					    collapse: false
					},
					context: {
						patient: {},
						events: [],
						eventId: undefined,
						channelType: undefined,
						receiverUrl: null
					},
					rules: {
						eventId: [
							{ required: true, message: 'Please select event', trigger: 'change' }
						]
					}
				};
			},
			mounted() {
				this.loading = true;
				axios.get("/current-context").then(response => {
					Object.assign(this.context, response.data, { receiverUrl });
				}).catch(error => {
					this.$message.error(errorMessage(error.response.data.message));
				}).finally(() => {
					this.loading = false;
				});
			},
			methods: {
				onChange() {
                    if(this.context.eventId && this.context.receiverUrl){
                        this.query("/preview",response => {
                            this.preview.text = response.data;
                            this.preview.loaded = true;
                        })
                    }
				},
				submitForm() {
				    this.$refs.patientForm.validate(valid => {
                        if (!valid) {
                            return false;
                        } else {
                            this.query("/send-alert",response => { this.$message.success(successMessage(response))});
                        }
				    })
				},
				query(url, handler){
                    this.loading = true;
                    var alertRequest = {
                        patientId : this.context.patient.id,
                        eventId: this.context.eventId,
                        channelType: this.context.channelType,
                        receiverUrl: this.context.receiverUrl
                    };
                    axios.post(url, alertRequest)
                        .then(handler)
                        .catch(error => {
                            this.$message.error(errorMessage(error.response.data.message));
                        }).finally(() => {
                            this.loading = false;
                        })
                }
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