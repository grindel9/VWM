import { Component, OnInit, Input } from '@angular/core';
import { BaseFilterCellComponent, FilterService } from '@progress/kendo-angular-grid';
import { CompositeFilterDescriptor } from '@progress/kendo-data-query';

@Component({
  selector: 'app-drop-down-brand-filter',
  templateUrl: './drop-down-brand-filter.component.html',
  styleUrls: ['./drop-down-brand-filter.component.css']
})
export class DropDownBrandFilterComponent extends BaseFilterCellComponent {

   public get selectedValue(): any {
        const filter = this.filterByField(this.valueField);
        return filter ? filter.value : null;
    }

    @Input() public filter: CompositeFilterDescriptor;
    @Input() public data: any[];
    @Input() public textField: string;
    @Input() public valueField: string;

    public get defaultItem(): any {
        return {
            [this.textField]: "All",
            [this.valueField]: null
        };
    }

    constructor(filterService: FilterService) {
        super(filterService);
    }

    public onChange(value: any): void {
        this.applyFilter(
            value === null ? // value of the default item
                this.removeFilter(this.valueField) : // remove the filter
                this.updateFilter({ // add a filter for the field with the value
                    field: this.valueField,
                    operator: 'eq',
                    value: value
                })
        ); // update the root filter
    }
}
