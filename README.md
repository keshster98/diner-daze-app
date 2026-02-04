# 🍽️ Restaurant Ordering App

## 📌 Project Overview
The **Restaurant Ordering App** is a mobile application designed for walk-in restaurant diners. It allows customers to browse menus, place orders digitally, and submit table-based orders without relying on manual order-taking. The app aims to improve dining experience, reduce ordering errors, and streamline restaurant operations.

This project was developed as a **Semester 5 Personal Project**.

---

## 🎯 Project Objectives
- Improve the dine-in customer experience through digital menu viewing and ordering  
- Reduce order errors by providing a structured and clear ordering flow  
- Explore the feasibility of a simple, low-setup digital ordering solution for small and medium-sized restaurants  

---

## 👥 Target Users
- **Primary users:** Walk-in restaurant diners  
- **Secondary users:** Restaurant administrators and staff  

---

## 🔐 Authentication & Registration
- Email and password authentication using **Firebase Authentication**
- New users must complete their profile immediately after registration
- Full access to app features is granted only after profile completion

---

## 🏠 Home Screen Structure
The Home Screen consists of multiple tabs:
- Menu
- News
- Cart *(logged-in users only)*
- Admin *(admin-only access)*
- User Profile
- Settings

---

## 📋 Menu Features
### Menu Listing
- Displayed in a vertical grid (two items per row)
- Each menu card shows:
  - Food name
  - Category (Starters, Salad, Main Course, etc.)
  - Price
  - Vegetarian / Non-Vegetarian tag
  - Spicy / Non-Spicy tag

### Search & Filters
- Strict search by food name only
- Filter by:
  - Category
  - Food type
  - Spiciness level
- Filters can be combined and used together with search

### Menu Item Details
- Preparation time
- Serving size
- Ingredients used
- Quantity selector and **Add to Cart** button  
  *(Hidden for non-logged-in users)*

---

## 📰 News Module
- Read-only announcements created by admins
- Used for:
  - Menu updates
  - Operating hours
  - Renovations and notices

### Features
- Search by post title (case-insensitive)
- Sort posts by newest or oldest

---

## 🛒 Cart & Ordering
*(Available to logged-in users only)*

### Cart Display
- Empty state when no items are added
- Displays selected items with individual prices
- Total price calculated automatically

### Table Number Requirement
- Users must enter a table number before checkout
- **Pay button is disabled** until table number is provided

### Payment Simulation
- Simulates a successful payment
- Displays confirmation:

  > “Payment received and order received.”

- Redirects user back to the Home Screen (News tab)

---

## 🛠️ Admin Panel
*(Admin-only access)*

### User Management
- View all registered users
- Strict search by user name
- Sorting options:
  - Registration date
  - Alphabetical order

### User Profile Control
- View and edit any user profile
- Manage account deletion requests

### Content Management
- Add, edit, and delete:
  - Menu items
  - News posts

---

## 👤 User Profile
- Displays complete user profile
- Users can edit all fields except email
- Email changes require admin assistance

---

## ⚙️ Settings
*(Logged-in users only)*

### Options
- Request account deletion
- Log out

### Account Deletion Flow
- User is immediately treated as non-logged-in
- Access to authenticated features is revoked
- Account remains in **pending deletion** state until admin approval

---

## 🧰 Tools & Technologies
- Android Studio IDE
- Android libraries and dependencies
- Firebase Authentication
- Android Studio documentation
- Free public APIs *(if Retrofit is required)*

---

## 🗓️ Implementation Timeline
### Week 1
- Authentication & registration
- Menu, News, Profile, Settings tabs
- Log out functionality

### Week 2
- Admin panel
- Account deletion request handling

### Week 3
- Rigorous testing
- Final commit to main branch
- APK release submission

---

## ⚠️ Limitations
- No table booking or pre-ordering
- No takeaway or pickup order option
- Inconsistencies with UI design

---

## 🚧 Challenges
- Designing a clean UI that hides filters when not in use

---

## 🔮 Future Enhancements
- Table booking and pre-order system
- Pickup / takeaway order support
- Chef role to accept or reject order
- Admin can see list of orders
- Users can see current order and history of orders
- Payment will be better simulated with a sandbox

---
