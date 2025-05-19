const functions = require("firebase-functions");
const admin = require("firebase-admin");
admin.initializeApp();

exports.sendNotification = functions.https.onCall(async (data, context) => {
  const token = data.token;
  const title = data.title;
  const body = data.body;

  if (!token || !title || !body) {
    throw new functions.https.HttpsError("invalid-argument", "Faltan datos en la solicitud.");
  }

  const message = {
    notification: {
      title: title,
      body: body
    },
    token: token
  };

  try {
    // Enviar notificación
    await admin.messaging().send(message);

    // Guardar en Firestore
    await admin.firestore().collection("notifications").add({
      title: title,
      body: body,
      toUserId: token,
      timestamp: admin.firestore.FieldValue.serverTimestamp()
    });

    return { success: true };
  } catch (error) {
    console.error("Error al enviar o guardar:", error);
    throw new functions.https.HttpsError("internal", "No se pudo enviar o guardar la notificación.");
  }
});
