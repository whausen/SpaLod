<template>
    <div class="navbar" :class="{ dark: isDarkMode }">
      <div class="telefon">
        <button class="hamburger-button" @click="togglemenu">
          <span></span>
          <span></span>
          <span></span>
        </button>
        <ul :class="['menuopen',menuAnimationClass]" @transitionend="onTransitionend">
          <li><button @click="navigateTo('spalodWFS')" :class="{ active: activeTab === 'spalodwfs' }">SpaLod API</button></li>
          <li><button @click="navigateTo('login')" :class="{ active: activeTab === 'login' }">Login</button> </li>
          <li><button @click="navigateTo('register')" :class="{ active: activeTab === 'register' }">Register</button> </li>
          <li><button @click="navigateTo('admin')" :class="{ active: activeTab === 'admin' }">Admin</button></li>
          <li><button @click="navigateTo('doc')" :class="{ active: activeTab === 'doc' }">Doc</button></li>
          <li><button @click="navigateTo('external_links')" :class="{ active: activeTab === 'external' }">External Links</button></li>
        </ul>
      </div>
      <button v-if="isAdmin" @click="logout()" class="navbar-title">Logout</button>
      <button v-if="!isLoggedIn" @click="navigateTo('login')" :class="{ active: activeTab === 'login' }">Login</button>
      <div class="computer">
        <button @click="navigateTo('spalodWFS')" :class="{ active: activeTab === 'spalodwfs' }">SpaLod API</button>
        <button @click="navigateTo('register')" :class="{ active: activeTab === 'register' }">Register</button>
        <button @click="navigateTo('admin')" :class="{ active: activeTab === 'admin' }">Admin</button>
        <button @click="navigateTo('doc')" :class="{ active: activeTab === 'doc' }">Doc</button>
        <button @click="navigateTo('external_links')" :class="{ active: activeTab === 'external' }">External Links</button>

      </div>
    
    </div>
    <div>

    </div>
  </template>
  
  <script>
  import $ from "jquery";

  export default {
    data() {
      return {
        activeTab: 'admin',
        isDarkMode: false,
        menuOpen: "menu-closed",
        isAdmin: false,
        menuAnimationClass: "",
        isLoggedIn: false,
        username: ""
      };
    },
    mounted() {
      this.detectDarkMode();
      window.matchMedia('(prefers-color-scheme: dark)').addListener(event => {
        this.isDarkMode = event.matches;
      });
      this.checkLoggedIn();
      //this.checkRole(); Used to check if user is an admin or not
    },
    methods: {
      detectDarkMode() {
        this.isDarkMode = window.matchMedia('(prefers-color-scheme: dark)').matches;
      },
      togglemenu(){
        this.menuOpen=this.menuOpen === "menu-closed" ? "menu-open" : "menu-closed";;
        this.menuAnimationClass = this.menuOpen === "menu-open" ? "slide-in-left" : "slide-out-left";
      },
      closeMenu(){
        this.menuOpen=false;
      },
      navigateTo(page) {
        this.activeTab = page;
        window.history.pushState({}, '', `/${page}`);
        this.currentPath = window.location.pathname;
        window.location.reload();
      },
      checkRole(){
        $.ajax({
          url: import.meta.env.VITE_APP_API_BASE_URL +'/admin',
          method: 'GET',
          xhrFields: {
            withCredentials: true
          },
          success: (response) => {
            this.isAdmin = true;
          },
          error: (error) => {
            //console.error(error);
            console.log(error);
          }
        })
      },
      checkLoggedIn() { 
        $.ajax({
          url: import.meta.env.VITE_APP_API_BASE_URL +'/status',
          method: 'GET',
          xhrFields: {
            withCredentials: true
          },
          success: (response) => {
            this.isLoggedIn = true;
            this.isAdmin =true; // Line to delete if we want to block normal users access
            if(localStorage.getItem("githubLog")==null)
            {
              this.getAccessToken();
            }
          },
          error: (error) => {
            //console.error(error);
            console.log(error);
          }
        })
      },
      getAccessToken() {
        $.ajax({
          url: import.meta.env.VITE_APP_API_BASE_URL +'/getGitUser',
          method: 'GET',
          xhrFields: {
            withCredentials: true
          },
          success: (response) => {
            localStorage.setItem("uuid",response);
            this.$emit('username-updated',response)
          },
          error: (error) => {
            // this.$notify({
            //     title: 'Session expired',
            //     text: 'If you are connected with github, please reconnect.',
            //     group:"notLoggedIn",
            //     type: 'error',
            //     duration: 2000 // notification will disappear after 5 seconds
            //   });
            console.error(error);
          }
        })
      }, logout() {
        alert("logout");
      // Call a function to remove the cookies
      this.removeCookies();
      
      // Redirect or perform any other actions after logout if needed
    },
    removeCookies() {
      $.ajax({
        url: import.meta.env.VITE_APP_API_BASE_URL + '/logout',
        method: 'POST',
        xhrFields: {
          withCredentials: true,
        },
        success: () => {
          this.isLoggedIn = false;
          this.isAdmin = false; // Reset isAdmin if needed
          // Any other logic to clean up after logout
        },
        error: (error) => {
          console.log(error);
        },
      });
    }
    }
  };
  </script>
  
  <style scoped>
  .navbar {
    display: flex;
    background-color: rgb(241, 241, 241);
    justify-content: space-between;
    align-items: center;
    padding: 10px;
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    border-bottom-left-radius: 10px;
    border-bottom-right-radius: 10px;
  }
  
  .navbar.dark {
    background-color: #1A202C;
    color: #fff;
  }
  
  .navbar.light {
    background-color:#fff;
    color: #1A202C;
  }

  .navbar-title {
    border: none;
    cursor: pointer;
    background-color: #EF4444;
    color: #fff;
    font-size: 18px;
    font-weight: bold;
    padding: 5px 10px;
    border-radius: 5px;
  }
  .computer{
    display: block;
    width: 100%;
    text-align: center;
  }
  .computer button{
    border: none;
    cursor: pointer;
    background-color:transparent;
    font-size: 18px;
    color: #1A202C;
    font-weight:750;
    padding: 5px 10px;
    border-radius: 5px;
    margin-left: 20px;
    transition: background-color 0.3s ease-in-out;
  }
  .navbar.dark .computer button{
    color: white;
  }
  .computer button:hover{
    background-color: #dee1e6;
  }
  .navbar.dark .computer button:hover{
    background-color: #4A5568;
    color: white;
  }
  .telefon{
    display: none;
  }

@media screen and (max-width: 768px){
  .navbar{
    position: fixed;
  }
  .telefon{
    display: block;
  }
  .computer{
    display: none;
  }
  .hamburger-button {
  position: relative;
  display: inline-block;
  cursor: pointer;
  padding: 30px 42px 10px 10px;
  border: 1px solid #000;
  border-radius: 5px;
  background-color: transparent;
  transition: background-color 0.2s ease-in-out;
}
.navbar.dark .hamburger-button{
  background-color: transparent;
  border: 1px solid white;
}
.hamburger-button span {
  position: absolute;
  display: block;
  width: 31px;
  height: 3px;
  background-color: #000;
  border-radius: 5px;
  transition: all 0.3s ease-in-out;
}
.navbar.dark .hamburger-button span{
  background-color: whitesmoke;
}

.hamburger-button span:nth-child(1) {
  top: 25%;
}

.hamburger-button span:nth-child(2) {
  top: 50%;
}

.hamburger-button span:nth-child(3) {
  top: 75%;
}
.slide-in-left {
  animation: slide-in-left 0.7s forwards;
}

.slide-out-left {
  animation: slide-out-left 0.7s forwards;
}

@keyframes slide-in-left {
  from {
    transform: translateX(0%);
  }
  to {
    transform: translateX(+100%);
  }
}

@keyframes slide-out-left {
  from {
    transform: translateX(100%);
  }
  to {
    transform: translateX(0%);
  }
}

ul {
  position: absolute;
  bottom:-90vh;
  top: 125%;
  left: -225px;
  padding: 10px;
  background-color: #fff;
  border: 1px solid #000;
  border-radius: 5px;
  box-shadow: 0px 2px 5px rgba(0,0,0,0.3);
}
ul button{
  padding: 10px 20px;
  border-radius: 5px;
  border: none;
  background-color: transparent;
  color: inherit;
  cursor: pointer;
  transition: background-color 0.2s ease-in-out;
  font-size: 18px;
  font-weight: bold;
  margin-top: 10px;
  width: 100%;
  text-align: left;
}
.navbar.dark ul{
  background-color:#4A5568;
}
.navbar.light ul{
  background-color: aliceblue;
}

ul li {
  margin: 20px 10px 20px 20px;
}
}
  </style>
  