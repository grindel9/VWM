import { Component, OnInit } from '@angular/core';
import { GridDataResult, DataStateChangeEvent, PageChangeEvent} from '@progress/kendo-angular-grid';
import { State , process } from '@progress/kendo-data-query';
import { FilterableListService } from './filterable-list.service';
import { Brand, Country } from '../model/api/dropdown'
import { Product } from '../model/product';
import { PRODUCTS } from '../data/products';



@Component({
  selector: 'app-filterable-list',
  templateUrl: './filterable-list.component.html',
  styleUrls: ['./filterable-list.component.css']
})
export class FilterableListComponent implements OnInit {

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
    type: null,
    status: null
  }

  // samodoplnujici se policka
  public brand: Array<Brand> = [];
  public country: Array<Country> = [];
  public model: Array<string> = [];
  public products: Product[] = PRODUCTS;

  public gridView: GridDataResult;
  public pageSize = 10;
  public skip = 0;

  // public gridData: GridDataResult = process(PRODUCTS, this.state);

  constructor(private service: FilterableListService) { 
    
  }

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

    // musi to byt v pipe jinak se prvne nctou prazdny producty a az pak se vykresli hlavni grid
    this.service.getProducts().subscribe(
      (data => {
        this.products = data;
        this.loadItems();
      })
    );  

  }

  public pageChange(event: PageChangeEvent): void {
    this.skip = event.skip;
    this.loadItems();
  }

  private loadItems(): void {
    this.gridView = {
      data: this.products.slice(this.skip, this.skip + this.pageSize),
      total: this.products.length
    }
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
    console.log("request filter:");
    console.log(this.item);
    this.service.postProducts(this.item).subscribe(
      response => {
        console.log(response);
        this.products = response;
        this.loadItems();
      },
      err =>console.log(err)
    )
    
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