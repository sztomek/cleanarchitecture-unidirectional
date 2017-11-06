package hu.sztomek.archdemo.presentation.screens.login;

import dagger.Component;
import hu.sztomek.archdemo.presentation.di.PresentationComponent;

@ForLogin
@Component(dependencies = { PresentationComponent.class }, modules = { LoginModule.class })
public interface LoginComponent {

    void inject(LoginActivity activity);

}
