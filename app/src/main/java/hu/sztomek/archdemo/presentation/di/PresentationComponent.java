package hu.sztomek.archdemo.presentation.di;

import dagger.Component;
import hu.sztomek.archdemo.common.PerScreen;
import hu.sztomek.archdemo.presentation.app.AppComponent;

@PerScreen
@Component(dependencies = AppComponent.class, modules = {PresentationModule.class, RouterModule.class})
public interface PresentationComponent extends PresentationInjects, PresentationExposes {
}
