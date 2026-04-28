<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="page dossier-pro-page">
    <div class="header">
        <h1>Dossiers professionnels</h1>
        <p>Consultez les dossiers professionnels relies a la demande ainsi que leur statut le plus recent.</p>
        <div class="badge">Demande selectionnee: #${demandeId}</div>
    </div>

    <c:if test="${not empty infoMessage}">
        <div class="alert alert-success">
            <strong>Information:</strong> ${infoMessage}
        </div>
    </c:if>

    <div class="form-card">
        <div class="form-actions top-actions">
            <a class="btn btn-secondary" href="/demandes">Retour aux demandes</a>
        </div>

        <c:choose>
            <c:when test="${not empty dossiersProfessionnels}">
                <table class="dossier-pro-table">
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Champ a fournir</th>
                        <th>Valeur</th>
                        <th>Statut le plus recent</th>
                        <th>Action</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${dossiersProfessionnels}" var="dossier">
                        <tr>
                            <td>${dossier.dossierProfessionnelId}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty dossier.champFournirLibelle}">
                                        ${dossier.champFournirLibelle}
                                    </c:when>
                                    <c:otherwise>
                                        Non renseigne
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty dossier.valeur}">
                                        ${dossier.valeur}
                                    </c:when>
                                    <c:otherwise>
                                        -
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty dossier.dernierStatutLibelle}">
                                        <span class="status-pill">${dossier.dernierStatutLibelle}</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="status-pill status-unknown">Aucun statut</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <button class="btn btn-primary btn-scanner js-open-upload-modal"
                                        type="button"
                                        data-dossier-id="${dossier.dossierProfessionnelId}">
                                    Scanner
                                </button>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:when>
            <c:otherwise>
                <div class="empty-message">Aucun dossier professionnel trouve pour cette demande.</div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<div class="upload-modal-backdrop" id="uploadModalBackdrop" hidden></div>
<div class="upload-modal" id="uploadModal" role="dialog" aria-modal="true" aria-labelledby="uploadModalTitle" hidden>
    <div class="upload-modal-header">
        <h2 id="uploadModalTitle">Upload des fichiers du dossier pro</h2>
        <button type="button" class="upload-close-btn" id="uploadModalCloseBtn" aria-label="Fermer">&times;</button>
    </div>

    <div class="upload-modal-body">
        <p class="upload-subtitle">Dossier professionnel #<span id="selectedDossierId">-</span></p>

        <form id="uploadForm" class="upload-form" action="/dossier-pro/scanner" method="post" enctype="multipart/form-data">
            <input type="hidden" id="selectedDossierIdInput" name="dossierProfessionnelId" value="">

            <div id="fileInputsContainer" class="file-inputs-container">
                <div class="file-input-row">
                    <label>Fichiers</label>
                    <input type="file" name="fichiers" multiple>
                </div>
            </div>

            <div class="upload-actions">
                <button type="button" class="btn btn-secondary" id="addFileInputBtn">Ajouter un autre champ de fichiers</button>
                <button type="submit" class="btn btn-primary">Uploader</button>
            </div>
        </form>
    </div>
</div>

<script>
    (function () {
        const openButtons = document.querySelectorAll('.js-open-upload-modal');
        const modal = document.getElementById('uploadModal');
        const backdrop = document.getElementById('uploadModalBackdrop');
        const closeBtn = document.getElementById('uploadModalCloseBtn');
        const dossierIdText = document.getElementById('selectedDossierId');
        const dossierIdInput = document.getElementById('selectedDossierIdInput');
        const form = document.getElementById('uploadForm');
        const addFileInputBtn = document.getElementById('addFileInputBtn');
        const fileInputsContainer = document.getElementById('fileInputsContainer');

        function openModal(dossierId) {
            dossierIdText.textContent = dossierId || '-';
            dossierIdInput.value = dossierId || '';
            modal.hidden = false;
            backdrop.hidden = false;
            document.body.classList.add('modal-open');
        }

        function closeModal() {
            modal.hidden = true;
            backdrop.hidden = true;
            document.body.classList.remove('modal-open');
        }

        openButtons.forEach(function (button) {
            button.addEventListener('click', function () {
                openModal(button.dataset.dossierId);
            });
        });

        closeBtn.addEventListener('click', closeModal);
        backdrop.addEventListener('click', closeModal);

        document.addEventListener('keydown', function (event) {
            if (event.key === 'Escape' && !modal.hidden) {
                closeModal();
            }
        });

        addFileInputBtn.addEventListener('click', function () {
            const row = document.createElement('div');
            row.className = 'file-input-row';
            row.innerHTML = '<label>Fichiers</label><input type="file" name="fichiers" multiple>';
            fileInputsContainer.appendChild(row);
        });

        form.addEventListener('submit', function () {
            document.body.classList.remove('modal-open');
        });
    })();
</script>
