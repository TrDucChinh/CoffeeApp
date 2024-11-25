# Project Name: Coffee App
---

## 1. Introduction
- Coffee App is a user-friendly application designed to make coffee ordering seamless and enjoyable. Key features include:
  - An optimized interface and smooth user experience, easily accessible to all customers, enabling easy exploration of coffee products.
  - Detailed product information, helping users make informed purchasing decisions.
  - Fast and convenient ordering with no limitations on time or location.
  - Personalized user experiences: each account can manage orders, save preferences, and review purchase history.
  
## 2. Working Model
Team operates according to the Scrum model, using Linear to manage tasks. All tasks are thoroughly kept track on Linear.

- Link Linear: [Linear](https://linear.app/coffeshop/join/a81f6301450ce305c5602b1a8ba82123?s=1)

Each week, team will have a meeting to review completed tasks, tackle any remaining problems and propose solutions for the following week. 

### Version Control Strategy
Team uses **Gitflow** to manage code. Each member will create a branch from `develop` to work on, naming the branches in the format `feature/function-name`. 

Once completing a branch, team will create a Pull Request to review code as well as merge it into develop.

- Main branches:
  - `master`: Contains stable code which has been thoroughly tested and reviewed.
  - `develop`: Contains latest code going through the test and review process.
  - `feature/`: Contains ongoing development code, which are short-lived and will be merged into develop once completing.

<div>
  <img src="https://github.com/gxdumplingg/ReadMe.Image/blob/gxdumplingg-coffee-app/gitflow-1.png">
</div>

At the end of each week, team will merge develop into main/master to release a new version.
## 3. Technologies
- Language: Kotlin
- Development Platform: Android
- Firebase (Firebase Realtime Database, Firebase BOM, Firebase Authentication, Firebase Google Auth)
- Glide
- Retrofit
- RecyclerView, Adapter
- Kotlin Coroutines
- Goong Map

## 4. Demo App
- Link download: [APK](https://drive.google.com/file/d/1ZoYQSUmqmvTIaRh82EOQ9O2wC58enlCY/view)
- **Sign in**, **Sign up** and **Forgot password**
<table>
  <tr>
    <td><img src="https://github.com/gxdumplingg/ReadMe.Image/blob/gxdumplingg-coffee-app/sign_in.png"></td>
    <td><img src="https://github.com/gxdumplingg/ReadMe.Image/blob/gxdumplingg-coffee-app/sign_up.png"></td>
    <td><img src="https://github.com/gxdumplingg/ReadMe.Image/blob/gxdumplingg-coffee-app/forgot_pw_1.png" ></td>
    <td><img src="https://github.com/gxdumplingg/ReadMe.Image/blob/gxdumplingg-coffee-app/forgot_pw_2.png"></td>
   </tr> 
</table>

- **Home Screen** and **Details Screen**
<table>
  <tr>
    <td><img src="https://github.com/gxdumplingg/ReadMe.Image/blob/gxdumplingg-coffee-app/home.png" width = 300></td>
    <td><img src="https://github.com/gxdumplingg/ReadMe.Image/blob/gxdumplingg-coffee-app/coffee_detail.png" width = 300></td>
    <td><img src="https://github.com/gxdumplingg/ReadMe.Image/blob/gxdumplingg-coffee-app/bean_detail.png" width = 300></td>
   </tr> 
</table>

- **Cart Screen**
<table>
  <tr>
    <td><img src="https://github.com/gxdumplingg/ReadMe.Image/blob/gxdumplingg-coffee-app/add_to_cart.png" width = 300></td>
    <td><img src="https://github.com/gxdumplingg/ReadMe.Image/blob/gxdumplingg-coffee-app/cart_screen.png" width = 300></td>
   </tr> 
</table>

- **Payment Screen**
<table>
  <tr>
    <td><img src="https://github.com/gxdumplingg/ReadMe.Image/blob/gxdumplingg-coffee-app/payment_screen.png" width = 300></td>
    <td><img src="https://github.com/gxdumplingg/ReadMe.Image/blob/gxdumplingg-coffee-app/map.png" width = 300></td>
   </tr> 
</table>

- **Favourite Screen**
<table>
  <tr>
    <td><img src="https://github.com/gxdumplingg/ReadMe.Image/blob/gxdumplingg-coffee-app/favourite.png" width = 300></td>
   </tr> 
</table>

- **Order History**
<table>
  <tr>
    <td><img src="https://github.com/gxdumplingg/ReadMe.Image/blob/gxdumplingg-coffee-app/order_history.png" width = 300></td>
    <td><img src="https://github.com/gxdumplingg/ReadMe.Image/blob/gxdumplingg-coffee-app/order_history_detail.png" width = 300></td>
   </tr> 
</table>

- **Profile**
<table>
  <tr>
    <td><img src="https://github.com/gxdumplingg/ReadMe.Image/blob/gxdumplingg-coffee-app/profile.png" width = 300></td>
    <td><img src="https://github.com/gxdumplingg/ReadMe.Image/blob/gxdumplingg-coffee-app/edit_profile.png" width = 300></td>
    <td><img src="https://github.com/gxdumplingg/ReadMe.Image/blob/gxdumplingg-coffee-app/change_pw.png" width = 300></td>
   </tr> 
</table>

## 5. References
- [Kotlin Documentation](https://kotlinlang.org/docs/home.html)
- [Android Documention](https://developer.android.com/reference/org/w3c/dom/Document)
- [Firebase Documention](https://firebase.google.com/docs)
- [Firebase Realtime Database](https://firebase.google.com/docs/database?hl=vi)
- [Firebase Authentication Documentation](https://firebase.google.com/docs/auth)
- [Goong Documentation](https://help.goong.io/kb/app/android/tich-hop-goong-map-sdk-vao-android/)
