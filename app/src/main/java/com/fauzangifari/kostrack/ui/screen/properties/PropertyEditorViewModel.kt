package com.fauzangifari.kostrack.ui.screen.properties

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fauzangifari.kostrack.domain.model.Property
import com.fauzangifari.kostrack.domain.model.PropertyType
import com.fauzangifari.kostrack.domain.usecase.auth.GetCurrentUserUseCase
import com.fauzangifari.kostrack.domain.usecase.property.GetPropertyByIdUseCase
import com.fauzangifari.kostrack.domain.usecase.property.SavePropertyUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class PropertyEditorState(
    val id: String = "",
    val name: String = "",
    val address: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val type: PropertyType = PropertyType.CAMPUR,
    val isLoading: Boolean = false,
    val nameError: String? = null,
    val addressError: String? = null,
    val isEditMode: Boolean = false
)

sealed class PropertyEditorEvent {
    object Success : PropertyEditorEvent()
    data class Error(val message: String) : PropertyEditorEvent()
}

class PropertyEditorViewModel(
    private val getPropertyByIdUseCase: GetPropertyByIdUseCase,
    private val savePropertyUseCase: SavePropertyUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PropertyEditorState())
    val uiState: StateFlow<PropertyEditorState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<PropertyEditorEvent>()
    val event = _event.asSharedFlow()

    fun loadProperty(id: String?) {
        if (id == null) {
            _uiState.value = PropertyEditorState(isEditMode = false)
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, isEditMode = true)
            getPropertyByIdUseCase(id).onSuccess { property ->
                _uiState.value = _uiState.value.copy(
                    id = property.id,
                    name = property.name,
                    address = property.address,
                    description = property.description ?: "",
                    imageUrl = property.imageUrl ?: "",
                    type = property.type,
                    isLoading = false
                )
            }.onFailure {
                _uiState.value = _uiState.value.copy(isLoading = false)
                _event.emit(PropertyEditorEvent.Error("Gagal memuat data properti"))
            }
        }
    }

    fun onNameChange(newName: String) {
        _uiState.value = _uiState.value.copy(name = newName, nameError = null)
    }

    fun onAddressChange(newAddress: String) {
        _uiState.value = _uiState.value.copy(address = newAddress, addressError = null)
    }

    fun onDescriptionChange(newDesc: String) {
        _uiState.value = _uiState.value.copy(description = newDesc)
    }

    fun onImageUrlChange(newUrl: String) {
        _uiState.value = _uiState.value.copy(imageUrl = newUrl)
    }

    fun onTypeChange(newType: PropertyType) {
        _uiState.value = _uiState.value.copy(type = newType)
    }

    fun saveProperty() {
        val currentState = _uiState.value
        var hasError = false

        if (currentState.name.isBlank()) {
            _uiState.value = _uiState.value.copy(nameError = "Nama properti tidak boleh kosong")
            hasError = true
        }
        if (currentState.address.isBlank()) {
            _uiState.value = _uiState.value.copy(addressError = "Alamat tidak boleh kosong")
            hasError = true
        }

        if (hasError) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                val user = getCurrentUserUseCase()
                    .filterNotNull()
                    .first()

                val property = Property(
                    id = currentState.id,
                    ownerId = user.id,
                    name = currentState.name,
                    address = currentState.address,
                    description = currentState.description,
                    imageUrl = if (currentState.imageUrl.isBlank()) null else currentState.imageUrl,
                    type = currentState.type
                )

                savePropertyUseCase(property)
                    .onSuccess {
                        _event.emit(PropertyEditorEvent.Success)
                    }.onFailure { e ->
                        _event.emit(PropertyEditorEvent.Error(e.message ?: "Gagal menyimpan properti"))
                    }
            } catch (e: Exception) {
                _event.emit(PropertyEditorEvent.Error("Gagal mendapatkan data user: ${e.message}"))
            } finally {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }
}
