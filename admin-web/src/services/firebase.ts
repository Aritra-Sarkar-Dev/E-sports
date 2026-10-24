/// <reference types="vite/client" />
import { initializeApp, getApps, getApp } from 'firebase/app';
import { getAuth } from 'firebase/auth';
import { getFirestore } from 'firebase/firestore';

// Environment variables configuration for Firebase
const env = (import.meta as any).env || {};
const firebaseConfig = {
  apiKey: env.VITE_FIREBASE_API_KEY || 'AIzaSyCHGp00EpgC60eLK5aY5j5S7GewL8P0XUU',
  authDomain: env.VITE_FIREBASE_AUTH_DOMAIN || 'gen-lang-client-0403132803.firebaseapp.com',
  projectId: env.VITE_FIREBASE_PROJECT_ID || 'gen-lang-client-0403132803',
  storageBucket: env.VITE_FIREBASE_STORAGE_BUCKET || 'gen-lang-client-0403132803.firebasestorage.app',
  messagingSenderId: env.VITE_FIREBASE_MESSAGING_SENDER_ID || '553781740992',
  appId: env.VITE_FIREBASE_APP_ID || '1:553781740992:web:3d67a220e492e6f12a7425',
};

const app = !getApps().length ? initializeApp(firebaseConfig) : getApp();

export const auth = getAuth(app);
const databaseId = 'ai-studio-android-neonstri-e5bf85ff-f3d9-4a39-9fd6-44e0110fc0a7';
export const db = (() => {
  try {
    return getFirestore(app, databaseId);
  } catch (e) {
    return getFirestore(app);
  }
})();
export default app;
