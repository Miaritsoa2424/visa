<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="photo-signature-page">
    <section class="photo-signature-hero">
        <h1>Photo par webcam et signature</h1>
        <p>
            Cette page permet de capturer une image depuis la webcam et de dessiner une signature avec la souris ou le doigt.
            Les deux donnees sont envoyees sous forme d'image au format base64 dans le formulaire.
        </p>
        <div class="status-row">
            <span class="status-pill">Webcam active pour capture</span>
            <span class="status-pill">Signature au canvas</span>
            <span class="status-pill">Type de page: ${empty pageTitle ? 'Visa' : pageTitle}</span>
        </div>
    </section>

    <c:if test="${not empty successMessage}">
        <div class="photo-signature-alert success">
            <strong>Succes:</strong> ${successMessage}
        </div>
    </c:if>

    <div class="photo-signature-grid">
        <section class="capture-card">
            <div class="card-header">
                <h2>Capture webcam</h2>
                <p>Active la camera, prends une photo et conserve-la avant validation.</p>
            </div>
            <div class="card-body">
                <div class="video-frame">
                    <video id="webcamVideo" autoplay playsinline muted></video>
                    <div id="cameraFallback" class="camera-placeholder" hidden>
                        Impossible d'activer la webcam. Verifiez les permissions du navigateur puis rechargez la page.
                    </div>
                </div>

                <div class="toolbar">
                    <button class="btn btn-primary" type="button" id="startCameraBtn">Demarrer la camera</button>
                    <button class="btn btn-secondary" type="button" id="capturePhotoBtn" disabled>Prendre la photo</button>
                    <button class="btn btn-muted" type="button" id="retakePhotoBtn" disabled>Reprendre</button>
                </div>
            </div>
        </section>

        <section class="signature-card">
            <div class="card-header">
                <h2>Signature manuscrite</h2>
                <p>Tracez votre signature directement dans la zone ci-dessous avec la souris, le stylet ou le doigt.</p>
            </div>
            <div class="card-body">
                <div class="signature-frame">
                    <canvas id="signatureCanvas"></canvas>
                    <div class="signature-hint">Commencez a dessiner ici</div>
                </div>

                <div class="toolbar">
                    <button class="btn btn-secondary" type="button" id="clearSignatureBtn">Effacer la signature</button>
                    <button class="btn btn-muted" type="button" id="saveSignatureBtn">Apercu signature</button>
                </div>
            </div>
        </section>

        <section class="preview-card">
            <div class="card-header">
                <h2>Apercu</h2>
                <p>Controlez ici l'image capturee et la signature avant envoi.</p>
            </div>
            <div class="card-body preview-grid">
                <div class="preview-frame">
                    <img id="photoPreview" alt="Apercu photo" hidden>
                    <div id="photoEmpty" class="preview-empty">Aucune photo capturee pour le moment.</div>
                </div>

                <div class="preview-meta">
                    <div class="preview-stat">
                        <span class="label">Photo</span>
                        <span id="photoStatus" class="value">En attente</span>
                    </div>
                    <div class="preview-stat">
                        <span class="label">Signature</span>
                        <span id="signatureStatus" class="value">En attente</span>
                    </div>
                </div>
            </div>
        </section>
    </div>

    <div class="photo-signature-form">
        <form id="photoSignatureForm" action="/demande/photo-signature" method="post">
            <input type="hidden" name="photoData" id="photoData" value="${photoData}">
            <input type="hidden" name="signatureData" id="signatureData" value="${signatureData}">
            <input type="hidden" name="id" value="${id}">

            <div class="photo-signature-form-actions">
                <a class="btn btn-secondary" href="/demandes">Retour</a>
                <button class="btn btn-primary" type="submit">Valider photo et signature</button>
            </div>
        </form>
    </div>
</div>

<script>
    (function () {
        const video = document.getElementById("webcamVideo");
        const fallback = document.getElementById("cameraFallback");
        const startCameraBtn = document.getElementById("startCameraBtn");
        const capturePhotoBtn = document.getElementById("capturePhotoBtn");
        const retakePhotoBtn = document.getElementById("retakePhotoBtn");
        const photoPreview = document.getElementById("photoPreview");
        const photoEmpty = document.getElementById("photoEmpty");
        const photoStatus = document.getElementById("photoStatus");
        const photoDataInput = document.getElementById("photoData");
        const signatureCanvas = document.getElementById("signatureCanvas");
        const clearSignatureBtn = document.getElementById("clearSignatureBtn");
        const saveSignatureBtn = document.getElementById("saveSignatureBtn");
        const signatureStatus = document.getElementById("signatureStatus");
        const signatureDataInput = document.getElementById("signatureData");

        let stream = null;
        let isDrawing = false;
        let hasSignature = Boolean(signatureDataInput.value);
        const context = signatureCanvas.getContext("2d");

        function resizeSignatureCanvas() {
            const rect = signatureCanvas.getBoundingClientRect();
            const ratio = window.devicePixelRatio || 1;
            const snapshot = signatureCanvas.toDataURL("image/png");
            const image = new Image();

            image.onload = function () {
                signatureCanvas.width = Math.max(1, Math.round(rect.width * ratio));
                signatureCanvas.height = Math.max(1, Math.round(rect.height * ratio));
                context.setTransform(ratio, 0, 0, ratio, 0, 0);
                context.lineWidth = 3;
                context.lineCap = "round";
                context.lineJoin = "round";
                context.strokeStyle = "#0f172a";
                context.clearRect(0, 0, rect.width, rect.height);
                if (hasSignature && image.complete && image.naturalWidth > 0) {
                    context.drawImage(image, 0, 0, rect.width, rect.height);
                }
            };

            image.src = snapshot;
        }

        function updateSignatureData() {
            if (!hasSignature) {
                signatureDataInput.value = "";
                signatureStatus.textContent = "En attente";
                return;
            }

            signatureDataInput.value = signatureCanvas.toDataURL("image/png");
            signatureStatus.textContent = "Capturee";
        }

        function clearSignature() {
            const rect = signatureCanvas.getBoundingClientRect();
            context.clearRect(0, 0, rect.width, rect.height);
            hasSignature = false;
            updateSignatureData();
        }

        function drawPoint(event) {
            const rect = signatureCanvas.getBoundingClientRect();
            return {
                x: event.clientX - rect.left,
                y: event.clientY - rect.top
            };
        }

        function startStroke(event) {
            event.preventDefault();
            isDrawing = true;
            const point = drawPoint(event);
            context.beginPath();
            context.moveTo(point.x, point.y);
        }

        function drawStroke(event) {
            if (!isDrawing) {
                return;
            }

            event.preventDefault();
            const point = drawPoint(event);
            context.lineTo(point.x, point.y);
            context.stroke();
            hasSignature = true;
        }

        function endStroke() {
            if (!isDrawing) {
                return;
            }

            isDrawing = false;
            context.closePath();
            updateSignatureData();
        }

        async function startCamera() {
            try {
                stream = await navigator.mediaDevices.getUserMedia({ video: { facingMode: "user" }, audio: false });
                video.srcObject = stream;
                video.hidden = false;
                fallback.hidden = true;
                capturePhotoBtn.disabled = false;
                startCameraBtn.disabled = true;
                retakePhotoBtn.disabled = true;
            } catch (error) {
                video.hidden = true;
                fallback.hidden = false;
                capturePhotoBtn.disabled = true;
                startCameraBtn.disabled = false;
            }
        }

        function stopCamera() {
            if (!stream) {
                return;
            }

            stream.getTracks().forEach(function (track) {
                track.stop();
            });
            stream = null;
            video.srcObject = null;
        }

        function capturePhoto() {
            const captureCanvas = document.createElement("canvas");
            const width = video.videoWidth || 1280;
            const height = video.videoHeight || 960;
            captureCanvas.width = width;
            captureCanvas.height = height;

            const captureContext = captureCanvas.getContext("2d");
            captureContext.drawImage(video, 0, 0, width, height);

            const dataUrl = captureCanvas.toDataURL("image/png");
            photoPreview.src = dataUrl;
            photoPreview.hidden = false;
            photoEmpty.hidden = true;
            photoDataInput.value = dataUrl;
            photoStatus.textContent = "Capturee";
            retakePhotoBtn.disabled = false;
        }

        function resetPhoto() {
            photoPreview.removeAttribute("src");
            photoPreview.hidden = true;
            photoEmpty.hidden = false;
            photoDataInput.value = "";
            photoStatus.textContent = "En attente";
            retakePhotoBtn.disabled = true;
        }

        startCameraBtn.addEventListener("click", startCamera);
        capturePhotoBtn.addEventListener("click", capturePhoto);
        retakePhotoBtn.addEventListener("click", function () {
            resetPhoto();
            startCamera();
        });
        clearSignatureBtn.addEventListener("click", clearSignature);
        saveSignatureBtn.addEventListener("click", updateSignatureData);

        signatureCanvas.addEventListener("pointerdown", startStroke);
        signatureCanvas.addEventListener("pointermove", drawStroke);
        signatureCanvas.addEventListener("pointerup", endStroke);
        signatureCanvas.addEventListener("pointerleave", endStroke);
        signatureCanvas.addEventListener("pointercancel", endStroke);

        window.addEventListener("resize", resizeSignatureCanvas);

        document.getElementById("photoSignatureForm").addEventListener("submit", function () {
            updateSignatureData();
            if (!photoDataInput.value && !photoPreview.hidden && photoPreview.src) {
                photoDataInput.value = photoPreview.src;
            }
            stopCamera();
        });

        resizeSignatureCanvas();
        updateSignatureData();

        if (photoDataInput.value) {
            photoPreview.src = photoDataInput.value;
            photoPreview.hidden = false;
            photoEmpty.hidden = true;
            photoStatus.textContent = "Capturee";
            retakePhotoBtn.disabled = false;
        }

        if (signatureDataInput.value) {
            hasSignature = true;
            const image = new Image();
            image.onload = function () {
                const rect = signatureCanvas.getBoundingClientRect();
                context.drawImage(image, 0, 0, rect.width, rect.height);
                signatureStatus.textContent = "Capturee";
            };
            image.src = signatureDataInput.value;
        }
    })();
</script>