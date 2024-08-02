output "AZURE_RESOURCE_GROUP" {
  value = azurerm_resource_group.resource_group.name
}
output "AZURE_FUNCTION_NAME" {
  value = module.quarkus_function.azure_function_app_name
}