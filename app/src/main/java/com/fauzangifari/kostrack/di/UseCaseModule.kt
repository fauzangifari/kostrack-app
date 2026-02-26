package com.fauzangifari.kostrack.di

import com.fauzangifari.kostrack.domain.usecase.auth.GetCurrentUserUseCase
import com.fauzangifari.kostrack.domain.usecase.auth.SignInUseCase
import com.fauzangifari.kostrack.domain.usecase.auth.SignOutUseCase
import com.fauzangifari.kostrack.domain.usecase.auth.SignUpUseCase
import com.fauzangifari.kostrack.domain.usecase.payment.GetPaymentsByPropertyUseCase
import com.fauzangifari.kostrack.domain.usecase.payment.GetPaymentsByTenantUseCase
import com.fauzangifari.kostrack.domain.usecase.payment.RecordPaymentUseCase
import com.fauzangifari.kostrack.domain.usecase.property.DeletePropertyUseCase
import com.fauzangifari.kostrack.domain.usecase.property.GetPropertiesUseCase
import com.fauzangifari.kostrack.domain.usecase.property.GetPropertyByIdUseCase
import com.fauzangifari.kostrack.domain.usecase.property.SavePropertyUseCase
import com.fauzangifari.kostrack.domain.usecase.room.DeleteRoomUseCase
import com.fauzangifari.kostrack.domain.usecase.room.GetRoomByIdUseCase
import com.fauzangifari.kostrack.domain.usecase.room.GetRoomsByPropertyUseCase
import com.fauzangifari.kostrack.domain.usecase.room.SaveRoomUseCase
import com.fauzangifari.kostrack.domain.usecase.session.IsLoggedInUseCase
import com.fauzangifari.kostrack.domain.usecase.session.SetLoggedInUseCase
import com.fauzangifari.kostrack.domain.usecase.tenant.AssignTenantUseCase
import com.fauzangifari.kostrack.domain.usecase.tenant.GetTenantsByPropertyUseCase
import com.fauzangifari.kostrack.domain.usecase.tenant.GetTenantsByRoomUseCase
import com.fauzangifari.kostrack.domain.usecase.tenant.RemoveTenantUseCase
import org.koin.dsl.module

val useCaseModule = module {
    // Auth
    factory { SignInUseCase(get()) }
    factory { SignUpUseCase(get()) }
    factory { SignOutUseCase(get(), get()) }
    factory { GetCurrentUserUseCase(get()) }

    // Session
    factory { IsLoggedInUseCase(get()) }
    factory { SetLoggedInUseCase(get()) }

    // Property
    factory { GetPropertiesUseCase(get(), get()) }
    factory { GetPropertyByIdUseCase(get()) }
    factory { SavePropertyUseCase(get()) }
    factory { DeletePropertyUseCase(get()) }

    // Room
    factory { GetRoomsByPropertyUseCase(get()) }
    factory { GetRoomByIdUseCase(get()) }
    factory { SaveRoomUseCase(get()) }
    factory { DeleteRoomUseCase(get()) }

    // Tenant
    factory { GetTenantsByPropertyUseCase(get()) }
    factory { GetTenantsByRoomUseCase(get()) }
    factory { AssignTenantUseCase(get(), get()) }
    factory { RemoveTenantUseCase(get(), get()) }

    // Payment
    factory { GetPaymentsByPropertyUseCase(get()) }
    factory { GetPaymentsByTenantUseCase(get()) }
    factory { RecordPaymentUseCase(get()) }
}
