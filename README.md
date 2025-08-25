# 🌏 World Story

**World Story** is a mobile story reading application built with **Kotlin (Android)** using **MVVM architecture**.  
It provides a modern platform to read and manage stories, supporting both **novels** and **comics**, with role-based access and a clean UI.

---

## ✨ Features

### 👤 User Roles
- **Guest**: Browse stories without login.  
- **Member**: Register/login, save favorites, track reading progress, comment, and rate stories.  
- **Author**: Create and manage own stories and chapters.  
- **Admin**: Full control over users, genres, stories, ratings, and comments.  

### 📚 Story & Reading
- Browse stories by category (novels/comics).  
- Search stories by title or keyword.  
- Save progress and continue reading from the last chapter.  
- Rate and comment on stories (with reply system).  
- Personal bookshelf & reading history.  

### 🛠️ Management (Admin/Author)
- Manage users (add, edit, delete, assign roles).  
- Manage genres (create, edit, delete).  
- Add, update, and remove stories & chapters.  
- Moderate comments and ratings.  

---

## 🛠 Tech Stack

- **Language**: Kotlin  
- **Architecture**: MVVM  
- **Database**: Firebase / Local SQLite (for caching)  
- **Cloud**: Google Firebase (Auth, Firestore, Storage)  
- **Min SDK**: 24  
- **Target SDK**: 35  

---

## 🚀 Installation

### Option 1: Run from source
1. Clone the repo:
   ```bash
   git clone https://github.com/HQDnocoding/World_Story.git
2. Open the project in Android Studio (2024.2.1 or higher).
3. Sync Gradle and wait for dependencies to install.
4. Add your cred.json file under:
5. Create and start a virtual device (Pixel 6 Pro recommended).
5. Run the app.
### Option 2: Install apk file
