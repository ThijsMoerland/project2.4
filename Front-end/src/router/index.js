import { createRouter, createWebHistory } from 'vue-router'

import Login from '../components/main/Login.vue';
import Transactions from '../components/transactions/Transaction.vue';
import ViewTransaction from '../components/transactions/ViewTransactions.vue';
import Accounts from '../components/employees/Accounts.vue';
import AccountInfo from '../components/customers/AccountInfo.vue';
import CustomerAccountOverview from '../components/customers/AccountOverview.vue';
import AllAccounts from '../components/employees/AllAccounts.vue';
import AccountInfoforEmployee from '../components/employees/AccountInfo.vue';
import CustomerCreateTransactions from '../components/customers/CreateTransaction.vue';
import CustomerWithdrawOrDeposit from '../components/customers/WithdrawOrDeposit.vue';
import QuestionCustomer from '../components/employees/QuestionCustomer.vue';
import AddBankAccount from '../components/employees/AddBankAccount.vue';
import AddUser from '../components/employees/addUser.vue';
import QuestionEmployee from '../components/employees/QuestionEmployee.vue';


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', component: Login },
    { path: '/transactions/:id' , component: Transactions, props: true },
    { path: '/viewTransaction/:iban/:id' , component: ViewTransaction, props: true },
    { path: '/employee/accounts/:id' , component: Accounts, props: true },
    { path: '/accountInfo' , component: AccountInfo},
    { path: '/customerAccountOverview' , component: CustomerAccountOverview, props: true},
    { path: '/allAccounts' , component: AllAccounts, props: true},
    { path: '/accountInfoforEmployee/:id' , component: AccountInfoforEmployee, props: true},
    { path: '/customer/createtransactions/:id', component: CustomerCreateTransactions, props: true},
    { path: '/customer/withdrawOrDeposit/:id', component: CustomerWithdrawOrDeposit, props: true},
    { path: '/employee/question', component: QuestionCustomer},
    { path: '/employee/AddBankAccount', component: AddBankAccount},
    { path: '/AddUser', component: AddUser},
    { path: '/question', component: QuestionEmployee},
  ]
})

export default router
 