<template>
    <headerNavigation />

    <body>
        <div id="container">
            <h1>How do you want to login?</h1>

            <div id="btns">
                <button id="btn" @click="goToEmployee()">Employee</button>
                <button id="btn" @click="goToCustomer()">Customer</button>
            </div>
        </div>
    </body>
    <footerNavigation />
</template>
  
<script>
import headerNavigation from '../main/Header.vue'
import footerNavigation from '../main/Footer.vue';

export default {
    header: {
        name: "header",
        components: {
            headerNavigation
        }
    },
    footer: {
        name: "footer",
        components: {
            footerNavigation
        }
    },
    methods: {
        goToEmployee() {
            localStorage.setItem("role", "EMPLOYEE")
            this.$router.push(`/allAccounts`);
        },
        goToCustomer() {
            localStorage.setItem("role", "CUSTOMER")
            this.$router.push(`/customerAccountOverview`);
        },
    },
    
};
</script>
  
<style>
@import '../../assets/css/questionEmployee.css';
</style>