import { Component, OnInit } from '@angular/core';
import { GridDataResult, DataStateChangeEvent} from '@progress/kendo-angular-grid';
import { State , process } from '@progress/kendo-data-query';
import { FilterableListService } from './filterable-list.service';
import { Brand, Country } from '../model/api/dropdown'
import { Product } from '../model/product';


@Component({
  selector: 'app-filterable-list',
  templateUrl: './filterable-list.component.html',
  styleUrls: ['./filterable-list.component.css']
})
export class FilterableListComponent implements OnInit {

  public state: State = {
    skip: 0,
    take: 20,
  };

  // filter properties

  public item:Filtermodel = {
    id: null,
    brand: null,
    madeIn: null,
    model: null,
    screenSizeMin: null,
    screenSizeMax: null,
    priceMin: null,
    priceMax: null,
    type: { t:null},
    status: { s:null}
  }

  // samodoplnujici se policka
  public brand: Array<Brand> = [];
  public country: Array<Country> = [];
  public model: Array<string> = [];
  public products: Array<Product> = [];

  // public gridData: GridDataResult = process(PRODUCTS, this.state);

  constructor(private service: FilterableListService) { }

  ngOnInit(): void {
    this.service.getBrands().subscribe(
      (data) => this.brand = data
    );  

    this.service.getCountries().subscribe(
      (data) => this.country = data
    );  

    this.service.getModels().subscribe(
      (data) => this.model = data
    );  

    this.service.getProducts().subscribe(
      (data) => this.products = data
    );  
  }

  public dataStateChange(state: DataStateChangeEvent): void {
    this.state = state;
    this.service.getProducts().subscribe(
      (data) => this.products = data
    );  
  }

  addToCart() {
   window.alert('Your product has been added to the cart!');
  }

  clearFilter() {
    this.item.id = null;
    this.item.brand= null,
    this.item.madeIn= null,
    this.item.model= null,
    this.item.screenSizeMin= null,
    this.item.screenSizeMax= null,
    this.item.priceMin= null,
    this.item.priceMax= null,
    this.item.type= { t:null},
    this.item.status= { s:null}
   }

  filter() {
    console.log("request:");
    console.log(this.item);
  }
}

export interface IFiltermodel{
  id: number,
  brand: any,
  madeIn: any,
  model: string,
  screenSizeMin: number,
  screenSizeMax: number,
  priceMin: number,
  priceMax: number,
  type:string,
  status:string
}

export class Filtermodel implements IFiltermodel{
  id: number;
  brand: any;
  madeIn: any;
  model: string;
  screenSizeMin: number;
  screenSizeMax: number;
  priceMin: number;
  priceMax: number;
  type:any;
  status:any;
}