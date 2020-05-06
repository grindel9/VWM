import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DropDownBrandFilterComponent } from './drop-down-brand-filter.component';

describe('DropDownBrandFilterComponent', () => {
  let component: DropDownBrandFilterComponent;
  let fixture: ComponentFixture<DropDownBrandFilterComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DropDownBrandFilterComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DropDownBrandFilterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
