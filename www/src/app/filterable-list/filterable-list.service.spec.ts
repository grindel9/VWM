import { TestBed } from '@angular/core/testing';

import { FilterableListService } from './filterable-list.service';

describe('FilterableListService', () => {
  let service: FilterableListService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FilterableListService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
