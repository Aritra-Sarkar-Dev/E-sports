/// <reference types="vite/client" />
import { initializeApp, getApps, getApp } from 'firebase/app';
import { getAuth } from 'firebase/auth';
import { getFirestore } from 'firebase/firestore';

// Environment variables configuration for Firebase
const env = (import.meta as any).env || {};
const firebaseConfig = {
  apiKey: env.VITE_FIREBASE_API_KEY || 'AIzaSyPlaceholder-FFArenaAdminKey',
  authDomain: env.VITE_FIREBASE_AUTH_DOMAIN || 'ff-arena-gaming.firebaseapp.com',
  projectId: env.VITE_FIREBASE_PROJECT_ID || 'ff-arena-gaming',
  storageBucket: env.VITE_FIREBASE_STORAGE_BUCKET || 'ff-arena-gaming.appspot.com',
  messagingSenderId: env.VITE_FIREBASE_MESSAGING_SENDER_ID || '1234567890',
  appId: env.VITE_FIREBASE_APP_ID || '1:1234567890:web:abcdef123456',
};

const app = !getApps().length ? initializeApp(firebaseConfig) : getApp();

export const auth = getAuth(app);
export const db = getFirestore(app);
export default app;
